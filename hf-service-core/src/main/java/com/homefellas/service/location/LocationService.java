package com.homefellas.service.location;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Ehcache;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.transaction.annotation.Transactional;

import com.homefellas.model.core.CacheContent;
import com.homefellas.model.location.County;
import com.homefellas.model.location.LocationAlias;
import com.homefellas.model.location.LocationSearchResult;
import com.homefellas.model.location.State;
import com.homefellas.model.location.Zip;
import com.homefellas.service.core.AbstractService;

import edu.emory.mathcs.backport.java.util.Collections;

public class LocationService extends AbstractService implements ILocationService {

//	public static final String DELIMETER = ", ";
	public static final String DELIMETER = ", ";
	public static final String LUECENE_CACHE = "LUECENE_CACHE";
//	public static final String NIOS_FILE = "./locationDirectory";
	public static final int MAX_RESULTS = 10;

	private ILocationDao locationDao;
	private Ehcache locationCache;
	private String locationLueceneIndexHome;
	
	public void setLocationDao(ILocationDao locationDao) {
		this.locationDao = locationDao;
	}

	public void setLocationCache(Ehcache locationCache) {
		this.locationCache = locationCache;
	}

	@Override
	public void onInit() {
		super.onInit();
	
//		loadLocationCache();
	}
	
	public List<LocationSearchResult> findLocation(String searchedData)
	{
		List<LocationSearchResult> returnedData = findLocation(searchedData, MAX_RESULTS);
		return returnedData;
	}
	
	@Transactional
	public List<LocationSearchResult> findLocation(String searchTerm, int results)
	{
	
		searchTerm = searchTerm+"*";
		
		try
		{
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
			
			File file = new File(locationLueceneIndexHome);
			Directory index;
			if (file.exists())
				index = NIOFSDirectory.open(file);
			else
				index = indexLocations(analyzer);
			
			
			//a. check contact by email
			Query q = new QueryParser(Version.LUCENE_43, "display", analyzer).parse(searchTerm);
			List<LocationSearchResult> foundResults = search(q, index, 1, results);
			
			return foundResults;
		}
		catch (IOException ioException)
		{
			System.out.println(ioException.getMessage());
			return Collections.emptyList();
		}
		catch (ParseException parseException)
		{
			System.out.println(parseException.getMessage());
			return Collections.emptyList();
		}
	}
	
	
	private List<LocationSearchResult> search(Query q, Directory index, int priority, int results) throws IOException
	{
		IndexReader reader = DirectoryReader.open(index);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(results, true);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    
	    List<LocationSearchResult> foundLocations = new ArrayList<LocationSearchResult>(hits.length);
	    
	    for(int i=0;i<hits.length;++i) {
	        int docId = hits[i].doc;
	        Document d = searcher.doc(docId);
	        String display = d.get("display");
	        String defaultZip = d.get("defaultZip");
	        
	        LocationSearchResult searchResult = new LocationSearchResult();
	        searchResult.setDefaultZip(defaultZip);
	        searchResult.setDisplay(display);
	        
	        foundLocations.add(searchResult);
	        
	    }
	    
	    reader.close();
	    
	    return foundLocations;
	}
	
//	@Transactional
//	public List<ICacheable> findLocation(String searchedData, int results)
//	{
//		if (locationCache==null || locationCache.get(LocationCacheKeyEnum.ALL_LOCATION)==null)
//		{
//			loadLocationCache();
//		}
//		
//		
//		List<CacheContent<ICacheable>> localListCacheContent = (List<CacheContent<ICacheable>>)locationCache.get(LocationCacheKeyEnum.ALL_LOCATION).getValue();
//		
//		//Orginal logic is in BinarySearchIndexStore.findStartsWith
//		List<ICacheable> startsWithList = new ArrayList<ICacheable>(results);
//		if(localListCacheContent != null && localListCacheContent.size() <= 2)
//		{
//			for(CacheContent<ICacheable> cacheContent : localListCacheContent) 
//			{
//				if(cacheContent.getKey().startsWith(searchedData))
//					startsWithList.add(cacheContent.getValue());
//			}
//			return startsWithList;
//		}
//		int startIndex = binarySearch(searchedData, localListCacheContent, true);
//		if(startIndex == -1)
//			return null;
//		boolean bFirstLoopBreak = false;
//		
//		for(int i =startIndex; i>=0; i--) 
//		{
//			CacheContent<ICacheable> cacheContent = localListCacheContent.get(i);
//			String key = cacheContent.getKey();
//			if(key.startsWith(searchedData)) 
//			{
//				if(i == 0) 
//				{
//					for(int k=i;k<=startIndex;k++)
//						startsWithList.add(localListCacheContent.get(k).getValue());
//				}
//			}
//			else 
//			{
//				for(int j=i+1;j<=startIndex;j++)
//					startsWithList.add(localListCacheContent.get(j).getValue());
//				break;
//			}
//			if(startsWithList.size() >= results){
//					bFirstLoopBreak = true;
//					break;
//			}
//		 }
//		if(!bFirstLoopBreak) 
//		{
//			for(int i =startIndex+1; i<localListCacheContent.size(); i++) 
//			{
//				CacheContent<ICacheable> cacheContent = localListCacheContent.get(i);
//				String key = cacheContent.getKey();
//				if(key.startsWith(searchedData)) 
//				{
//					startsWithList.add(localListCacheContent.get(i).getValue());
//				}
//				else 
//				{
//					break;
//				}
//				if(startsWithList.size() >= results) 
//					break;
//			}
//		}
//		return startsWithList;
//	}
//	
//	private int binarySearch(String searchString, List<CacheContent<ICacheable>> list, boolean isStartsWith) 
//	{	
//		if(list==null || list.size()==0) return -1;
//		boolean cont = true;
//		int endPivot = list.size() - 1;
//		int startPivot = 0;
//		do 
//		{
//			int pivot = startPivot + ((endPivot - startPivot) / 2);
//			// endPivot:"+endPivot+" pivot:"+pivot);
//			if (!isStartsWith && list.get(pivot).getKey().equals(searchString)) 
//			{
//					return pivot;
//			} 
//			else if(list.get(pivot).getKey().startsWith(searchString)) 
//			{
//				return pivot;
//			} 
//			else 
//			{
//				if (startPivot == endPivot)
//					cont = false;
//				else if (startPivot > endPivot)
//					cont = false;
//				else if (isStringGreaterThan(searchString, list.get(pivot).getKey()))
//					startPivot = pivot + 1;
//				else
//					endPivot = pivot - 1;
//			}
//		} while (cont);
//		return -1;
//	}
//	
//	private boolean isStringGreaterThan(String value1, String value2) 
//	{
//		char[] val1 = value1.toCharArray();
//		char[] val2 = value2.toCharArray();
//		int length = (val2.length < val1.length ? val2.length : val1.length);
//		for (int i = 0; i < length; i++) 
//		{
//			if (val1[i] > val2[i])
//				return true;
//			else if (val1[i] < val2[i])
//				return false;
//		}
//		if (val1.length > val2.length)
//			return true;
//		else
//			return false;
//	}
	
	@Transactional
	public List<Zip> getZips()
	{
		return locationDao.getZips();
	}
	
	@Transactional
	public List<State> getStates()
	{
		return locationDao.getStates();
	}
	
	@Transactional
	public List<County> getCounties()
	{
		return locationDao.getCounties();
	}
	
	@Transactional
	public List<LocationAlias> getCityAliases()
	{
		return locationDao.getCityAliases();
	}
	
	@Transactional
	public List<LocationAlias> getStateAliases()
	{
		return locationDao.getStateAliases();
	}
	
	@Transactional
	public List<LocationAlias> getCountyAliases()
	{
		return locationDao.getCountyAliases();
	}
	
	private Directory indexLocations(StandardAnalyzer analyzer) throws IOException
	{
//		Element element;
//		if ((element = locationCache.get(LUECENE_CACHE)) != null)
//		{
//			return (Directory) element.getObjectValue();
//		}
		
//		if (index.fileExists(NIOS_FILE))
//			return index;		
//		Directory index = new RAMDirectory();
		
		Directory index = new NIOFSDirectory(new File(locationLueceneIndexHome));
		
		synchronized (LUECENE_CACHE)
		{
			//index contacts
//			
			
			
			
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
	
			IndexWriter w = new IndexWriter(index, config);
			
			String key;
			List<Zip> zips = getZips();
			List<CacheContent<LocationSearchResult>> locations = new ArrayList<CacheContent<LocationSearchResult>>(zips.size());
			Set<String> dupChecker = new HashSet<String>(zips.size());
			for (Zip zip : zips)
			{
				//build display name
				key = (zip.getCity() + DELIMETER + zip.getState().getName()).toLowerCase();
				
				//check to see if the cityState name was loaded
				if (!dupChecker.contains(key))
				{
					//we have not yet accessed it so first we need to add it to the set so we dont re-add it
					dupChecker.add(key);
					
					//we also need to add it to the location list
//					locations.add(new CacheContent(key, zip));
					Document doc = new Document();
					doc.add(new TextField("display", key, Field.Store.YES));
					doc.add(new StringField("defaultZip", zip.getDefaultZip(), Field.Store.YES));
					w.addDocument(doc);
					
				}
			}
			
			//clear the dupchecker
			dupChecker.clear();
			
			List<LocationAlias> cityAliases = getCityAliases();
			for (LocationAlias cityAlias: cityAliases) {
				
				Document doc = new Document();
				doc.add(new TextField("display", cityAlias.getAlias(), Field.Store.YES));
				doc.add(new StringField("defaultZip", cityAlias.getDefaultZip(), Field.Store.YES));
				w.addDocument(doc);
			}
			
			List<LocationAlias> stateAliases = getStateAliases();
			for (LocationAlias stateAlias: stateAliases) {
				locations.add(new CacheContent(stateAlias.getAlias().toLowerCase(), stateAlias));
				
				Document doc = new Document();
				doc.add(new TextField("display", stateAlias.getAlias(), Field.Store.YES));
				doc.add(new StringField("defaultZip", stateAlias.getDefaultZip(), Field.Store.YES));
				w.addDocument(doc);
								
			}
			
			w.commit();
			w.close();
			
			
			
			
//			locationCache.put(new Element(LUECENE_CACHE, index));
			
			return index;
		}
	}

	public String getLocationLueceneIndexHome()
	{
		return locationLueceneIndexHome;
	}

	public void setLocationLueceneIndexHome(String locationLueceneIndexHome)
	{
		this.locationLueceneIndexHome = locationLueceneIndexHome;
	}
	
//	@Transactional
//	public synchronized void loadLocationCache()
//	{
//		logger.info("Priming Location Cache");
//		
//		String key;
//		
//		List<Zip> zips = getZips();
//		List<CacheContent<ICacheable>> locations = new ArrayList<CacheContent<ICacheable>>(zips.size());
//		Set<String> dupChecker = new HashSet<String>(zips.size());
//		for (Zip zip : zips)
//		{
//			//build display name
//			key = (zip.getCity() + DELIMETER + zip.getState().getName()).toLowerCase();
//			
//			//check to see if the cityState name was loaded
//			if (!dupChecker.contains(key))
//			{
//				//we have not yet accessed it so first we need to add it to the set so we dont re-add it
//				dupChecker.add(key);
//				
//				//we also need to add it to the location list
//				locations.add(new CacheContent(key, zip));
//			}
//			
//			
//		}
//		
//		//clear the dupchecker
//		dupChecker.clear();
//		
//		//loop through counties now
////		List<County> counties = getCounties();
////		for (County county : counties)
////		{
////			key = (county.getName() + DELIMETER + county.getState().getName()).toLowerCase();
////			if (!dupChecker.contains(key))
////			{
////				//add the key to the dup checker
////				dupChecker.add(key);
////				
////				//add the county to cache
////				locations.add(new CacheContent(key, county));				
////			}
////		}
//		
//		//loop through the states now
////		List<State> states = getStates();
////		for (State state:states)
////		{
////			key = state.getCode() + DELIMETER + state.getCountry().getName();
////			locations.add(new CacheContent(key, state));
////			
////			key = state.getName() + DELIMETER + state.getCountry().getName();
////			locations.add(new CacheContent(key, state));
////			
////		}
//		
//		//handle aliases
//		//city alias
//		List<LocationAlias> cityAliases = getCityAliases();
//		for (LocationAlias cityAlias: cityAliases) {
//			locations.add(new CacheContent(cityAlias.getAlias().toLowerCase(), cityAlias));
//		
//		}
//		// county alias
////		List<LocationAlias> countyAliases = getCountyAliases();
////		for (LocationAlias countyAlias : countyAliases) {
////			locations.add(new CacheContent(countyAlias.getAlias().toLowerCase(), countyAlias));
////		}
//		// state alias
//		List<LocationAlias> stateAliases = getStateAliases();
//		for (LocationAlias stateAlias: stateAliases) {
//			locations.add(new CacheContent(stateAlias.getAlias().toLowerCase(), stateAlias));
//		}
//		
//		Collections.sort(locations, new SortByKey());
//		locationCache.put(new Element(LocationCacheKeyEnum.ALL_LOCATION, locations));
//	}
//	
//	private class SortByKey implements Comparator<CacheContent> {
//		public int compare(CacheContent a, CacheContent b) {
//			if (a.getKey() != null && b.getKey() != null)
//				return a.getKey().compareTo(b.getKey());
//			return -1;
//		}
//	}
	
	

}
