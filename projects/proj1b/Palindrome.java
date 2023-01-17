public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        LinkedListDeque<Character> newdeque = new LinkedListDeque<>();
        for (int i = 0; i <= word.length() - 1; i += 1) {
            newdeque.addLast(word.charAt(i));
        }
        return newdeque;
    }

    public boolean isPalindrome(String word) {
        if (word == null) {
            return false;
        }
        //else if  (word.length() == 1 || word.length() == 0) {
        //return true;}
        Deque<Character> deque = wordToDeque(word);
        while (deque.size() > 1) {
            if (deque.removeFirst() != deque.removeLast()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word == null) {
            return false;
        }
        Deque<Character> deque = wordToDeque(word);
        while (deque.size() > 1) {
            if (!cc.equalChars(deque.removeFirst(), deque.removeLast())) {
                return false;
            }
        }
        return true;
    }
}
