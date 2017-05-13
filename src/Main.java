import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		while(true){
			Scanner s = new Scanner(System.in);
			System.out.println("Please select an operation number." + "\n"+
							   "1-) Search a prefix string" + "\n" +
							   "2-) Exit program!");
			int	number = s.nextInt();
			s.nextLine();
			if(number==1){
				Scanner inputFileName = new Scanner(System.in);
				System.out.println("Enter the name of the text file to be searched!");
				String file = inputFileName.next();
				Scanner inputTopK = new Scanner(System.in);
				System.out.println("How many strings are sorted according to priority?");
				int topK = inputTopK.nextInt();
				while(true){
					Scanner s1 = new Scanner(System.in);
					System.out.println("Please select an operation number." + "\n"+
									   "1-) Give a prefix string" + "\n" +
									   "2-) Give a new file name and k value." + "\n" +
									   "3-) Exit search and go main menu!");
					int	number1 = s1.nextInt();
					s1.nextLine();
					if(number1 == 3){
						break;
					}
					else if(number1 == 1){
						Scanner inputKey = new Scanner(System.in);
						System.out.println("Enter the prefix string you want to search!");
						String key = inputKey.nextLine();
						if(key.equals("")){
							throw new java.lang.NullPointerException("give a prefix String..! Try Again");
						}
						File path = new File( file.trim());
						ArrayList<Words> allWords = new ArrayList<Words>();
						ArrayList<Words> wordsIncludeKey = new ArrayList<Words>();
						readFile(path,allWords);													/* Go read file function */
						quickSortArrayListForWord(allWords, 0, allWords.size()-1);					/* Array list sorting for word */
						int aWordLocation = binarySearchForKey(allWords,0 , allWords.size(), key);	/* Find a index that is start with prefix string in array list */
						if(aWordLocation >= 0){														/* if ArrayList have any word go on */
							allWordForPrefixString(wordsIncludeKey,allWords,aWordLocation,key);		/* Find all words start with prefix string and add new  array list*/
							quickSortArrayListForWeight(wordsIncludeKey, 0, wordsIncludeKey.size()-1);		/* new array list sorting for weight */	
							System.out.println("% " + file + " " + topK + "\n" + key);		
							for(int i = wordsIncludeKey.size()-1 ; i>=wordsIncludeKey.size()-topK ; i--){
								if(i>=0){
									System.out.println(wordsIncludeKey.get(i).getWeight() + " " + wordsIncludeKey.get(i).getWord() );
									if(Long.parseLong(wordsIncludeKey.get(i).getWeight()) < 0)
										throw new java.lang.IllegalArgumentException("Weight is not negative..!");	/* Weight is not negative and throw illegal argument exception */
								}
								else
									break;
							}
							System.out.println();
						}
						else{
							System.out.println("There are no words that start with an prefix string!");	/* ArrayList haven't word start with prefix string */
						}
						
					}else{
						break;
					}
					
				}
			}
			else{
				System.out.println("Good Bye...");
				break;
			}
		}
	}
	
	public static void allWordForPrefixString(ArrayList<Words> wordsIncludeKey,ArrayList<Words> allWords,int aWordLocation,String key){
		Words newWord;			/* This function performs a linear search to the right and left of the index value that comes with binary search. */
		for(int i = aWordLocation ; i>=0 ; i--){	/* Checks values less than index and adds to new list	*/
			if(allWords.get(i).getWord().startsWith(key)){	/* Control start with prefix string	*/
				newWord = new Words();
				newWord.setWord(allWords.get(i).getWord());
				newWord.setWeight(allWords.get(i).getWeight());
				wordsIncludeKey.add(newWord);
			}
			else{
				break;		/* if one of the words does not start with a string anymore end the loop */
			}
		}
		for(int i = aWordLocation +1 ; i<allWords.size() ; i++){	/* Checks values greater than index and adds to new list	*/
			if(allWords.get(i).getWord().startsWith(key)){		/* Control start with prefix string	*/
				newWord = new Words();
				newWord.setWord(allWords.get(i).getWord());
				newWord.setWeight(allWords.get(i).getWeight());
				wordsIncludeKey.add(newWord);
			}
			else{
				break;	/* if one of the words does not start with a string anymore end the loop */
			}
		}
	}
	
	public static int binarySearchForKey(ArrayList<Words> allWords , int low , int high , String key){
		int middlePoint = (low + high) /2 ;		/* Calculate mid point */
		while(low<=high){
			if(allWords.get(middlePoint).getWord().compareTo(key) < 0){
				low = middlePoint +1;	/* Look right side in ArrayList */
			}
			else if(allWords.get(middlePoint).getWord().startsWith(key)){
				return middlePoint;		/* Find a word which is starting prefix string and return location index */
			}
			else{
				high = middlePoint -1;	/* Look left side in ArrayList*/
			}
			middlePoint = (low+high)/2;	/* Calculate new mid point */
		}
		return -1;	/* if any word not start prefix string , return -1 and that is main any word not starting prefix string */
	}
	
	public static void quickSortArrayListForWeight(ArrayList<Words> wordsIncludeKey,int low, int high){
		/* This quick sort function sorting array list for weight value */
		int i = low;
		int j = high;
		long pivot = Long.parseLong(wordsIncludeKey.get(low + (high -low)/2).getWeight());	/* select a pivot weight in array list */
		while(i <= j){
			while(Long.parseLong(wordsIncludeKey.get(i).getWeight()) < pivot)	/* Compare words and pivot */
				i++;
			while(Long.parseLong(wordsIncludeKey.get(j).getWeight()) > pivot)	/* Compare words and pivot */
				j--;
			if(i<=j){
				Collections.swap(wordsIncludeKey, i, j);			/* Swap two word in array list */
				i++;	
				j--;
			}	
		}
		if(low<j)
			quickSortArrayListForWeight(wordsIncludeKey, low, j);	/* Recursive function look left side  */
		if(i<high)
			quickSortArrayListForWeight(wordsIncludeKey, i,high);	/* Recursive function look right side  */
	}
	
	public static void quickSortArrayListForWord(ArrayList<Words> allWords,int low , int high){
		/* This quick sort function sorting array list for word alphabetical sequence */
		int i = low;
		int j = high;
		String pivot = allWords.get(low + (high -low)/2).getWord();		/* select a pivot word in array list */
		while(i <= j){
			while(allWords.get(i).getWord().compareTo(pivot) < 0)		/* Compare words and pivot */
				i++;
			while(allWords.get(j).getWord().compareTo(pivot) > 0)		/* Compare words and pivot */
				j--;
			if(i<=j){
				Collections.swap(allWords, i, j);				/* Swap two word in array list */
				i++;
				j--;
			}		
		}
		if(low<j)
			quickSortArrayListForWord(allWords, low, j);		/* Recursive function look left side  */
		if(i<high)
			quickSortArrayListForWord(allWords, i,high);		/* Recursive function look right side  */
	}
	
	public static void readFile(File path,ArrayList<Words> allWords){	
		try{
			FileReader inputFile = new FileReader(path);
			BufferedReader  bufferReader = new BufferedReader(inputFile);
			String line;
			line = bufferReader.readLine();	/* first line jumping in text file because this line does not contain important information */
			while((line = bufferReader.readLine()) != null){
				if(!line.isEmpty())
					splitLine(line, allWords);	/* Every line go function for split and adding array list */
			}
			bufferReader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void splitLine(String line, ArrayList<Words> allWords){
		Words newWord  = new Words();
		String[] splitString = line.split("[\\t\\\n]");		/* line split determines which are tab and newline */
		if(!splitString[1].isEmpty() && !splitString[0].isEmpty()){	/* every line include two information*/
			newWord.setWord(splitString[1]);	/* Second information is word */
			newWord.setWeight(splitString[0].trim());	/* First information is weight */
			allWords.add(newWord);	/* Add new Word in array list */
		}
	}
}