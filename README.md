# FindDuplicates
Java implementation to find duplicate files

Simple command line execution:
------------------------------------

javac FindDuplicates.java

java FindDuplicates C:\Users\<your name>\Downloads\test


Background:
===========

I often had a big issue with duplicate files in my pc. I had a lot of dslr pictures backed up in different folders and sometimes renamed. 
The solution was to scan and delete all duplicate files. But identifying duplicate files were more complicated than just checking for files with same name, the check needed to actually check the file itself. Because the dslr pics might have same name but be different files. And they might have different names, but maybe the same picture. Hence I built this tool in order to check every file in a very efficient way so that t takes only few seconds to check hundred of filed and gigabytes of data.
