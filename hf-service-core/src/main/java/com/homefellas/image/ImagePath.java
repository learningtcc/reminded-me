package com.homefellas.image;

import java.io.File;

import com.homefellas.model.core.AbstractBaseTO;

public abstract class ImagePath extends AbstractBaseTO {

	private static final long serialVersionUID = -6652042205605938034L;
	private String imageName;
	
	private IImageMapper imageMapper;
	
	private File image;
	
	private boolean enabled=true;
	
	public abstract String getContext();
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
//	public String getMemberDirectory()
//	{
//		return "//"+memberTO.getId();
//	}
	public String getRelativePathToImage()
	{
		return getUploadDirectory()+imageName;
	}

	public String getUploadDirectory()
	{
		return getContext()+File.separator+imageMapper.getId()+File.separator;
	}
	
	public String getImageDirectory()
	{
		return getContext()+"/"+imageMapper.getId()+"/"+imageName;
	}
	
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}
	public IImageMapper getImageMapper() {
		return imageMapper;
	}
	public void setImageMapper(IImageMapper imageMapper) {
		this.imageMapper = imageMapper;
	}
	
}
