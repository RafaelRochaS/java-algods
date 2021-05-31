public class HashTablesTester {
    
    public static void main(String[] args) {
        
        HashTables ht = new HashTables();

        System.out.println("Setting values...");

        ht.put("key 1", "value 1");
        ht.put("key 2", "value 2");
        ht.put("key 3", "value 3");

        System.out.println("Value in key 1: " + ht.get("key 1"));
        System.out.println("Value in key 3: " + ht.get("key 3"));
        System.out.println("Size: " + ht.size());
        System.out.println("Removing key 2...");
        
        ht.remove("key 2");

        System.out.println("Size: " + ht.size());
        System.out.println("Contains key 2: " + ht.containsKey("key 2"));
        System.out.println("Contains key 3: " + ht.containsKey("key 3"));

    }
}
