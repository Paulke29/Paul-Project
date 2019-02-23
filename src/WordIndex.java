import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.io.Writer;

/**
 * A special type of {@link Index} that indexes the locations words were found.
 */

public class WordIndex implements Index<String>{
	HashMap<String,HashSet<Integer>> answer = new HashMap<>();
	HashSet<Integer>index;
	private static TreeMap<String,TreeMap<String,TreeSet<Integer>>> wordsindex ;
	private TreeMap<String,TreeMap<String,TreeSet<Integer>>> fileindex = new TreeMap<>();
	/**
	 * 
	 * @param words
	 * @param file
	 * @return the position of words with stem in file
	 */
	public WordIndex() {
		wordsindex = new TreeMap<>();
	}
	public TreeMap<String,TreeMap<String,TreeSet<Integer>>> getWordsindex(Path file)throws IOException{
		return index(file);
	}
	public TreeMap<String,TreeMap<String,TreeSet<Integer>>> index(Path file) throws IOException {
		
		int number = 1;
		for(String words : TextFileStemmer.stemFile(file)) {
			if(!wordsindex.containsKey(words)) {
				TreeSet<Integer> position = new TreeSet<>();
				position.add(number);
				TreeMap<String,TreeSet<Integer>> textindex = new TreeMap<>();
				textindex.put(file.toString(), position);
				wordsindex.put(words,textindex);
			}
			else{
				
				if(!wordsindex.get(words).containsKey(file.toString())) {
					TreeSet<Integer> position = new TreeSet<>();
					position.add(number);
					wordsindex.get(words).put(file.toString(),position);
					
				}
				if(!wordsindex.get(words).get(file.toString()).contains(number)) {
					wordsindex.get(words).get(file.toString()).add(number);
				}
			}
			number ++;
			
		}
		
//		System.out.println(wordsindex.size());
		return wordsindex;
	}
	public TreeMap<String,Integer>word_count(Path file) throws IOException{
		TreeMap<String,Integer> counting  = new TreeMap<>();
		HashSet<Path> files = new HashSet<>();
		Writer writer = null;
		files.addAll(Traverse_directoru.traverse_file(file));
		int number = 0;
		int level = 0;
		for(Path counting_words : files) {
			number = TextFileStemmer.stemFile(counting_words).size();
			counting.put(counting_words.toString(), number);
		}
		return counting;
		
	}
	@Override
	public boolean add(String element, int position) {
		// TODO Auto-generated method stub
		if(!answer.containsKey(element)) {
			index = new HashSet<>();
			index.add(position);
			answer.put(element, index);
			return true;
		}
		else {
			if(answer.containsKey(element) && !answer.get(element).contains(position)) {
				answer.get(element).add(position);
				return true;
			}
			else {
				return false;
			}
		}
	}

	@Override
	public int numPositions(String element) {
		int number = 0;
		if(!answer.containsKey(element)) {
			return 0;
		}
		// TODO Auto-generated method stub
		else {
			if(answer.get(element) == null) {
				return 0;
			}
			else {
				
				return index.size();
			}
			
		}
		
	}

	@Override
	public int numElements() {
		// TODO Auto-generated method stub
		
		return answer.keySet().size();
	}

	@Override
	public boolean contains(String element) {
		// TODO Auto-generated method stub
		if(answer.containsKey(element)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean contains(String element, int position) {
		// TODO Auto-generated method stub
		if(!answer.containsKey(element)) {
			return false;
		}
		else {
			for(Integer a : answer.get(element)) {
				if(a == position) {
					return true;
					}
				}
		
		}
		return false;
		
	}

	@Override
	public Collection<String> getElements() {
		// TODO Auto-generated method stub
		try {
			HashSet<String> elements = new HashSet<>();
			elements.addAll(answer.keySet());
			Collection<String> newlist = Collections.unmodifiableCollection(elements);
			return newlist;
		}catch(UnsupportedOperationException e) {
			return(Collection<String>) e;
		}
	}

	@Override
	public Collection<Integer> getPositions(String element) {
		// TODO Auto-generated method stub
		try {
			HashSet<Integer> position = new HashSet<>();
			if(!answer.containsKey(element)) {
				Collection<Integer> newlist = Collections.unmodifiableCollection(position);
				return newlist;
			}
			else {
				for(Integer positions : answer.get(element)) {
					position.add(positions);
				}
				Collection<Integer> newlist = Collections.unmodifiableCollection(position);
				return newlist;
				
			}
		}catch(UnsupportedOperationException e) {
			return(Collection<Integer>) e;
		}	
	}

	
	/*
	 * TODO Modify anything within this class as necessary. This includes the class
	 * declaration; you need to implement the Index interface!
	 */

}
