# CSArrayList Report

## Part A – Safety Fixes
- Made `indexOf(Object)` null-safe (avoids `NullPointerException`).
- Added `remove(Object)` that returns `true` when an element is removed and `false` otherwise.
- Verified index bounds in `add(int, E)`, `get(int)`, and `remove(int)`.

**Invariant maintained:**  
`0 <= size <= capacity`

---

## Part B – Fail-Fast Iterator
- Implemented inner `Itr` class with `checkForComodification()` and `expectedModCount`.
- Added `modCount++` in all structural changes (`add`, `remove`).
- Iterator correctly throws `ConcurrentModificationException` when list is modified during iteration.

---

## Part C – JUnit 5 Tests

| Test | Description | Result |
|------|--------------|--------|
| `edgeIndexCases()` | Checks boundary behavior for add/get/remove | ✅ Passed |
| `multipleResizes()` | Confirms resizing works with > 10,000 elements | ✅ Passed |
| `searchesWithDuplicatesAndNulls()` | Tests `indexOf` with duplicates and nulls | ✅ Passed |
| `removeObjectBehavior()` | Tests `remove(Object)` correctness | ✅ Passed |
| `failFastIterator()` | Verifies iterator throws `ConcurrentModificationException` | ✅ Passed |
| `microbenchmarkAppendAndGet()` | Performance benchmark (disabled by default) | ⚠️ Ignored |

---

## Part D – Benchmark & Complexity

### ⚙️ Big-O Complexity

| Operation | Time Complexity | Explanation |
|------------|----------------|--------------|
| `add(E)` | **Amortized O(1)** | Occasional reallocation doubles capacity |
| `add(int, E)` | **O(n)** | Must shift elements right |
| `get(int)` | **O(1)** | Direct array access |
| `set(int, E)` | **O(1)** | Direct assignment |
| `remove(int)` | **O(n)** | Shifts elements left |
| `remove(Object)` | **O(n)** | Linear search, then remove |
| `indexOf(Object)` | **O(n)** | Linear search |
| `iterator()` | **O(1)** | Returns lightweight iterator |

---

### ⏱️ Benchmark Results

| List Type | N (elements) | Append (ns) | Get (ns) |
|------------|---------------|-------------|----------|
| `CSArrayList` | 200,000 | 10,423,800 | 909,700 |
| `ArrayList` | 200,000 | 2,754,500 | 1,045,400 |

**Interpretation:**
- Both implementations have similar asymptotic complexity (`O(1)` amortized append, `O(1)` get).
- Java’s `ArrayList` is faster for append operations because it’s heavily optimized by the JVM.
- `CSArrayList` performs comparably, showing correct behavior and expected order of magnitude for each operation.

---

## Part E – Design Notes & Reflection

**Key design choices**
- **Dynamic resizing:** Doubled array capacity (`capacity *= 2`) to balance speed and memory use.
- **modCount tracking:** Ensures fail-fast iterators detect concurrent modifications.
- **Null handling:** Implemented null-safe comparison in `indexOf(Object)` to prevent `NullPointerException`.

**Challenges**
- Getting `modCount` increments in the correct spots so the iterator test passed.
- Debugging unreachable statement errors when first implementing `indexOf`.
- Understanding how the iterator’s `expectedModCount` and `modCount` interact.

---

## ✅ Summary
- Implemented all required features.
- All 5 functional tests **passed**.
- 1 benchmark test **ignored by default** (enabled locally for timing).
- Behavior and complexity match Java’s built-in `ArrayList`.

