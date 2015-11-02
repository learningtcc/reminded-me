package com.homefellas.login.authenication.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.scribe.up.profile.UserProfile;
import org.scribe.up.profile.facebook.FacebookProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.leleuj.ss.oauth.client.authentication.OAuthAuthenticationToken;
import com.homefellas.cas.client.authentication.ProfileJson;
import com.homefellas.core.SpringBean;
import com.homefellas.exception.DatabaseNotInitializedException;
import com.homefellas.exception.ValidationException;
import com.homefellas.user.IUserService;
import com.homefellas.user.IUserServiceTX;
import com.homefellas.user.OAuthModel;
import com.homefellas.user.Profile;
import com.homefellas.util.AbsolutePathPropertyPlaceholderConfigurer;

@Controller
@RequestMapping("/facebook") 
public class FaceBookProvider extends SpringBean
{

	@Autowired
	private IUserServiceTX userService;
	
	@Value("${cas.oauth.profile.url}")
	private String casProfileUrl;
		
	@RequestMapping(method = RequestMethod.GET)
    public String proccessFacebookRequest(HttpServletRequest request) {
        
        OAuthAuthenticationToken token = (OAuthAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        
        UserProfile userProfile = token.getUserProfile();
     
        // facebook profile
        FacebookProfile facebookProfile = (FacebookProfile) userProfile;
             
        String email = facebookProfile.getEmail();
        String id = facebookProfile.getId();
        String firstName = facebookProfile.getFirstName();
        String lastName = facebookProfile.getFamilyName();
        
        if (email==null||id==null||email.equals("")||id.equals(""))
        {
        	throw new RuntimeException("Facebook returned null id or email");
        }
        OAuthModel authModel = new OAuthModel();
        authModel.setEmail(email);
        authModel.setLastName(lastName);
        authModel.setFirstName(firstName);
        authModel.setId(id);
        Profile profile;
		try
		{
			profile = userService.getProfileOrRegisterFaceBookUserTX(authModel);
			
			 ProfileJson profileJson  = new ProfileJson();
		     profileJson.setId(profile.getId());

		     Assertion assertion = new AssertionImpl(new AttributePrincipalImpl(profileJson.getId()));
				 
			//set it back in session
		    HttpSession session = request.getSession();
			session.setAttribute("_const_cas_assertion_", assertion);
		}
		catch (ValidationException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		catch (DatabaseNotInitializedException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
        
		String redirectURl = "redirect:"+casProfileUrl+"/facebook/"+authModel.getId();
        System.out.println(redirectURl);
        return redirectURl;
    }
	
	
}
