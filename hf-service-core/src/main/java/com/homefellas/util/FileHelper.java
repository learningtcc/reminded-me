package com.homefellas.util;

import java.io.File;
import java.io.IOException;

public class FileHelper
{

	public static void deleteFolder(String pathToFile)
	{
		File directory = new File(pathToFile);
		if (!directory.exists())
		{

			System.out.println("Directory does not exist.");
			
		}
		else
		{

			try
			{

				FileHelper.deleteFolder(directory);

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	public static void deleteFolder(File file) throws IOException
	{

		
		
		if (file.isDirectory())
		{

			// directory is empty, then delete it
			if (file.list().length == 0)
			{

				file.delete();
				System.out.println("Directory is deleted : "
						+ file.getAbsolutePath());

			}
			else
			{

				// list all the directory contents
				String files[] = file.list();

				for (String temp : files)
				{
					// construct the file structure
					File fileDelete = new File(file, temp);

					// recursive delete
					deleteFolder(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0)
				{
					file.delete();
					System.out.println("Directory is deleted : "
							+ file.getAbsolutePath());
				}
			}

		}
		else
		{
			// if file, then delete it
			file.delete();
			System.out.println("File is deleted : " + file.getAbsolutePath());
		}
	}
}
