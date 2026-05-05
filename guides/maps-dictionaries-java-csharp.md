# Maps (Dictionaries) in Java vs. C#

Both languages provide robust collections for storing key-value pairs, but they utilize different naming conventions and methods.

## Java: `HashMap`
In Java, the standard interface is `Map<K, V>` and the most common implementation is `HashMap`.

```java
import java.util.HashMap;
import java.util.Map;

Map<String, Integer> map = new HashMap<>();
map.put("Alice", 25);
map.put("Bob", 30);

// Retrieving a value (returns null if not found, though auto-unboxing can throw NullPointerException)
int age = map.get("Alice");

// Simplified safe retrieval (returns a custom default value if key is not found)
int customDefaultAge = map.getOrDefault("Charlie", -1);

// Checking if a key exists
boolean hasAlice = map.containsKey("Alice");
```

## C#: `Dictionary`
In C#, the equivalent interface is `IDictionary<TKey, TValue>` and the standard implementation is `Dictionary<TKey, TValue>`.

```csharp
using System.Collections.Generic;

Dictionary<string, int> dictionary = new Dictionary<string, int>();
dictionary.Add("Alice", 25);
// Alternatively, using the indexer:
dictionary["Bob"] = 30;

// Retrieving a value
int age = dictionary["Alice"];

// Checking if a key exists
bool hasAlice = dictionary.ContainsKey("Alice");

// Safe retrieval (prevents KeyNotFoundException)
if (dictionary.TryGetValue("Alice", out int safeAge)) {
    // safeAge is successfully retrieved
}

// Simplified safe retrieval (returns default value if key is not found, e.g., 0 for int)
// Note: If the dictionary stored objects or strings, this would return null instead.
int defaultAge = dictionary.GetValueOrDefault("Charlie"); 

// Simplified safe retrieval with a custom default value
int customDefaultAge = dictionary.GetValueOrDefault("Charlie", -1);
```
