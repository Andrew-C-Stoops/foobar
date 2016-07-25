/*
    Spy snippets
    ============

    You've been recruited by the team building Spy4Rabbits, a highly advanced search engine used to help fellow
    agents discover files and intel needed to continue the operations against Dr. Boolean's evil experiments.
    The team is known for recruiting only the brightest rabbit engineers, so there's no surprise they brought you
    on board. While you're elbow deep in some important encryption algorithm, a high-ranking rabbit official requests
    a nice aesthetic feature for the tool called "Snippet Search." While you really wanted to tell him how such a
    feature is a waste of time in this intense, fast-paced spy organization, you also wouldn't mind getting kudos
    from a leader. How hard could it be, anyway?

    When someone makes a search, Spy4Rabbits shows the title of the page. Your commander would also like it to show
    a short snippet of the page containing the terms that were searched for.

    Write a function called answer(document, searchTerms) which returns the shortest snippet of the document,
    containing all of the given search terms. The search terms can appear in any order.

    The length of a snippet is the number of words in the snippet. For example, the length of the snippet
    "tastiest color of carrot" is 4. (Who doesn't like a delicious snack!)

    The document will be a string consisting only of lower-case letters [a-z] and spaces. Words in the string will
    be separated by a single space. A word could appear multiple times in the document.
    searchTerms will be a list of words, each word comprised only of lower-case letters [a-z]. All the search
    terms will be distinct.

    Search terms must match words exactly, so "hop" does not match "hopping".

    Return the first sub-string if multiple sub-strings are shortest. For example, if the document is
    "world there hello hello where world" and the search terms are ["hello", "world"],
    you must return "world there hello".

    The document will be guaranteed to contain all the search terms.

    The number of words in the document will be at least one, will not exceed 500, and each word will
    be 1 to 10 letters long. Repeat words in the document are considered distinct for counting purposes.
    The number of words in searchTerms will be at least one, will not exceed 100, and each word will
    not be more than 10 letters long.
 */


package spy_snippets;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Answer {
    private static final int first = 0;
    private static final int last = 1;
    private static Integer[] findShortestIndices(Map<String, List<Integer>> indices,
                                             String[] searchTerms, int termIndex, int start, int end) {
        // select one index from each search term
        // initially it is beginning and ending index
        // go deeper in the search terms until find final indices
        // keep updating beginning and ending index
        // out of all solutions return the solution with least number of indices
        // return it

        // Sample thinking:
        //test(indices, searchTerms, 1, begin, end)
        //a: [0,3] = test(indices, searchTerms, 1, 0, 0)
        //   [2,4] = test(indices, searchTerms, 1, 4, 4)
        //   if (termIndex==0):
        //     from the arraylist find first least and return [2,4]
        //...
        //c: test(indices, searchTerms, 2, 0, 2)
        //   test(indices, searchTerms, 2, 0, 5)
        //   iterate through arraylist of results and return the first least,
        //   return [0, 3]
        //
        //   recursively test(indices, searchTerms, 2, 2, 4)
        //   recursively test(indices, searchTerms, 2, 4, 5)
        //   return [2, 4]
        //d: return [0, 3]
        //   return [0, 5]
        //   return [2, 4]
        //   return [2, 5]

        List<Integer[]> intervals = new ArrayList<Integer[]>();
        for (Integer index : indices.get(searchTerms[termIndex])) {
            if (termIndex != searchTerms.length-1) {
                int minIndex = (index < start || start < 0) ? index : start;
                int maxIndex = (index > end || end < 0) ? index : end;

                intervals.add(findShortestIndices(indices, searchTerms, termIndex + 1, minIndex, maxIndex));
            } else {
                int minIndex = (start < index) ? ((start >= 0) ? start : index) : index;
                int maxIndex = (end > index) ? end : index;
                intervals.add(new Integer[]{minIndex, maxIndex});
            }
        }

        Integer[] min = new Integer[2];
        int minLength = -1;
        for (Integer[] interval : intervals) {
            int length = interval[last] - interval[first];
            if (length < minLength || minLength < 0) {
                minLength = length;
                min = interval;
            }
        }

        return min;
    }

    public static String answer(String document, String[] searchTerms) {
        // Separate the word list
        String[] wordList = document.split(" ");

        // Initialise the hash map to store term indices
        Map<String, List<Integer>> indices = new HashMap<String, List<Integer>>();
        for (String term : searchTerms) {
            indices.put(term, new ArrayList<Integer>());
        }

        // Add the indices
        List termList = Arrays.asList(searchTerms);
        for (int index = 0; index < wordList.length; ++index) {
            if (termList.contains(wordList[index])) {
                indices.get(wordList[index]).add(index);
            }
        }

        Integer[] answer = findShortestIndices(indices, searchTerms, 0, -1, -1);

        String response = "";
        for (int index = answer[first]; index <= answer[last]; ++index) {
            response += wordList[index];
            if (index != answer[last]) {
                response += " ";
            }
        }

        return response;
    }

    public static void main(String[] args) {
//        String document = "many google employees can program";
//        String[] searchTerms = {"google", "program"};
        // google: 1
        // program: 4
        // combinations: [1-4]
        // shortest combination: [1-4] => "google employees can program"

//        String document = "a b c d a";
//        String[] searchTerms = {"a", "c", "d"};
        // a: 0, 4
        // c: 2
        // d: 3
        // combinations: [0-3], [2-4]
        // shortest combination: [2-4] => "c d a"

//        String document = "world there hello hello where world";
//        String[] searchTerms = {"hello", "world"};
        // hello: 2, 3
        // world: 0, 5
        // combinations: [2-0], [2-5], [3-0], [3-5]
        // shortest combinations: [0-2] => "world there hello", [3-5] => "hello where world"
        // shortest combination: [0-2] => "world there hello"

        String document = "Lorem Ipsum is simply dummy text of the printing and typesetting industry Lorem Ipsum " +
                "has been the industrys standard dummy text ever since the 1500s when an unknown printer took a " +
                "of type and scrambled it to make a type specimen book It has survived not only five centuries but " +
                "also the leap into electronic typesetting remaining essentially unchanged It was popularised in " +
                "the 1960s with the release of Letraset sheets containing Lorem Ipsum passages and more recently " +
                "with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum";
        String[] searchTerms = {"the", "industry", "Ipsum"};

        String response = answer(document, searchTerms);
        System.out.println("response = " + response);
    }
}