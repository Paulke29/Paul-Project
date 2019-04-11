import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;


/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2019
 */ 
public class Driver {
	/**
	 * 
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 * 
	 */
	public static void main(String[] args) {

		Instant start = Instant.now();
		ArgumentMap argumentMap = new ArgumentMap(args);
		InvertedIndex wordindex = new InvertedIndex();
		InvertedIndexBuilder invertedIndexBuilder = new InvertedIndexBuilder();
		searchResult ResultSearch = new searchResult(wordindex);

		if (argumentMap.hasFlag("-path")) {

			try {
				if (argumentMap.hasValue("-path")) {
					Path path = argumentMap.getPath("-path");
					invertedIndexBuilder.filesIndex(TextFileFinder.list(path), wordindex);
				}
			} catch (IOException e) {
				System.out.println("Couldn't to print index from path");
			}
		}
		if (argumentMap.hasFlag("-index")) {

			Path indexPath = argumentMap.getPath("-index", Path.of("index.json"));
			try {
				wordindex.nestJSON(indexPath);
			} catch (IOException e) {
				System.out.println("Couldn't get anything from path: " + indexPath);
			}

		}
		if (argumentMap.hasFlag("-locations")) {

			Path locationPath = argumentMap.getPath("-locations", Path.of("locations.json"));
			try {
				wordindex.locationsJSON(locationPath);
			} catch (IOException e) {
				System.out.println("Couldn't get anything  from path: " + locationPath);
			}
		}
		if (argumentMap.hasFlag("-query")) {
			if (argumentMap.hasValue("-query")) {
				Path query = argumentMap.getPath("-query");
				try {
					boolean exact = false;
					if(argumentMap.hasFlag("-exact")) {
						exact = true;
					}
					ResultSearch.SearchResult(exact, query);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (argumentMap.hasFlag("-results")) {
				Path result = argumentMap.getPath("-results",Path.of("results.json"));
				try {
					ResultSearch.toJSON(result);
				} catch (IOException e) {
					System.out.println(e);
				}
		}
		if(argumentMap.hasFlag("-threads")) {
			Path threads = argumentMap.getPath("-threads");
		}
		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}
}
