 
		To enable security on a client... 
		1.  Add the filter to the web.xml
		<filter>
        	<filter-name>springSecurityFilterChain</filter-name>
        	<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    	</filter>
    	<filter-mapping>
      		<filter-name>springSecurityFilterChain</filter-name>
      		<url-pattern>/*</url-pattern>
    	</filter-mapping>
    	
    	2.  Add com/homefellas/context/security-client-context.xml to the war you are trying to enable security on	
    	  This context needs the following beans:
    	  a.  casValidationFilter  
	    	<bean id="casValidationFilter" class="org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter">
			 	<property name="serverName" value="${hf.core.server.name}" />
	        	<property name="exceptionOnValidationFailure" value="true" />
	        	<property name="redirectAfterValidation" value="true" />
	        	<property name="ticketValidator" ref="ticketValidator" />
			</bean>
		  b.  springSecurityFilterChain - This is used to defined what need to be secured.
		  	<bean id="springSecurityFilterChain" class="org.springframework.security.web.FilterChainProxy">
        		<sec:filter-chain-map path-type="ant">
	            	<sec:filter-chain pattern="/" filters="casValidationFilter, wrappingFilter" />
	            	<sec:filter-chain pattern="/secure/receptor" filters="casValidationFilter" />
	            	<sec:filter-chain pattern="/j_spring_security_logout" filters="logoutFilter,etf,fsi" />
	            	<sec:filter-chain pattern="/rest/**/" filters="casAuthenticationFilter, casValidationFilter, anonymousAuthFilter,wrappingFilter, sif,j2eePreAuthFilter,logoutFilter,etf,fsi"/>
					<sec:filter-chain pattern="/**/*_secure" filters="casAuthenticationFilter, casValidationFilter, anonymousAuthFilter,wrappingFilter, sif,j2eePreAuthFilter,logoutFilter,etf,fsi"/> 
        		</sec:filter-chain-map>
  			</bean>
  		  c.  fsi - FilterSecurityInterceptor is used to lock certain paths to certain roles
  		  	<bean id="fsi" class="org.springframework.security.web.access.intercept.FilterSecurityInterceptor">
        		<property name="authenticationManager" ref="authenticationManager"/>
        		<property name="accessDecisionManager" ref="httpRequestAccessDecisionManager"/>
        		<property name="securityMetadataSource">
            		<sec:filter-invocation-definition-source>
            			<sec:intercept-url pattern="/rest/**" access="ROLE_HF_USER"/>
						<sec:intercept-url pattern="/rest/core/**" access="ROLE_ADMIN"/>
					</sec:filter-invocation-definition-source>
        		</property>
    		</bean>		
		
		3.  Include security-context.xml in your applicationContext.xml (<import resource="classpath:main/resource/conf/security-context.xml"/>)
		
		4.  Add login.properties to the properties loader (most likely properties-context.xml).  <value>login.properties</value>
		
		5.  set the mapping in the springSecurityFilterChain to whatever pattern you want
		
		6.  Add record to registerdserviceimpl table for the new path
		
		To Test RESTful security
		1.  Post to /login/v1/tickets and pass username=tdelesio@gmail.com&password=test12
   		2.  This will return a TGT (ticket granting ticket)
   		3.  Post to /login/v1/tickets/{TGT} and pass service=https://localhost/ws-core/rest/core/jsonize/com.homefellas.rm.task.Task
   			The service you pass should be https and the resource that you want to access.
   		4.  This will return a ST (service ticket).  You can now call the original url and pass the ST as ticket={ST}
   		
   		To Setup login WAR
   		CREATE TABLE locks (
 			application_id VARCHAR(50) NOT NULL,
 			unique_id VARCHAR(50) NULL,
 			expiration_date TIMESTAMP NULL
		);
		
		ALTER TABLE locks ADD CONSTRAINT pk_locks
 		PRIMARY KEY (application_id);
 
 		CREATE INDEX ST_TGT_FK_I ON SERVICETICKET (ticketGrantingTicket_ID);
 		CREATE INDEX ST_TGT_FK_I ON TICKETGRANTINGTICKET (ticketGrantingTicket_ID);
 
 	INSERT INTO `registeredserviceimpl`(`id`,`allowedToProxy`,`anonymousAccess`,`description`,`enabled`,`evaluation_order`,`ignoreAttributes`,`name`,`serviceId`,`ssoEnabled`,`theme`) VALUES (1,1,0,'Only Allows HTTP Urls',1,10000001,0,'HTTP','http://**',1,null);
	INSERT INTO `registeredserviceimpl`(`id`,`allowedToProxy`,`anonymousAccess`,`description`,`enabled`,`evaluation_order`,`ignoreAttributes`,`name`,`serviceId`,`ssoEnabled`,`theme`) VALUES (2,1,0,'Only Allows HTTPS Urls',1,10000002,0,'HTTPS','https://**',1,null);
	
	
	To Setup OAUTH
	1. To setup oauth you need to first add a row to the service registry. (either hte database or the inmemory). The service id needs to be the clients url.  The name will be the key and the 
	description will be the sercet.  
	INSERT INTO `registeredserviceimpl`(`id`,`allowedToProxy`,`anonymousAccess`,`description`,`enabled`,`evaluation_order`,`ignoreAttributes`,`name`,`serviceId`,`ssoEnabled`,`theme`) VALUES (4,1,0,'the_secrect_for_caswrapper1',1,1000002,0,'the_key_for_caswrapper1','http://localhost/ws-core',1,null);
	2. A record will also need to be entered for the callbackAuthorize.
	INSERT INTO `registeredserviceimpl`(`id`,`allowedToProxy`,`anonymousAccess`,`description`,`enabled`,`evaluation_order`,`ignoreAttributes`,`name`,`serviceId`,`ssoEnabled`,`theme`) VALUES (3,1,0,'oauth wrapper callback url',1,1000001,0,'HTTP','https://localhost/login/oauth2.0/callbackAuthorize',1,null); 
	
	
	To Test OAUTH
    1.  When the client needs to make a request, they will call:
	https://localhost/login/oauth2.0/authorize?client_id=the_key_for_caswrapper1&redirect_uri=http://localhost/ws-core/rest/core/default
	The client_id (which is the name from the service) and the return url (which must match the service id) must be passed.  This will redirect the user to the redirect url and pass a code.
	2. This code must be picked up by the client and you must pass the code to the below sample url passing hte secret key, key, code, and redirect url:
	https://localhost/login/oauth2.0/accessToken?code=ST-3-aXR9EfblD2WDdxl70RNP-cas&client_id=the_key_for_caswrapper1&client_secret=the_secrect_for_caswrapper1&redirect_uri=http://localhost/ws-core/index.html
	3. From this call an access token will be granted.  You can then call specific urls on the api by passing the access token so for example:
	https://localhost/login/oauth2.0/profile?access_token=TGT-1-JMIFBHWIQRnIudlDzsqNuDQw3KAXa5CK2uOJziV7sphOvnLakn-cas
	