# Report: Assignment 2 - Data Structures

## 1. Asymptotic Complexity Table

| Structure | Operation | Best Case | Average Case | Worst Case | Auxiliary Space | Justification |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **DynamicArray** | `get(i)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | Direct pointer arithmetic offset calculation. |
| | `add(x)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(n)$ | $\Theta(1)$ | Amortized constant time; worst case triggers full 2x array reallocation. |
| | `add(i, x)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Best case at tail; average/worst requires right-shifting elements. |
| | `remove(i)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Best case at tail; average/worst requires left-shifting elements. |
| | `contains(x)`| $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Best case element is at index 0; otherwise scans entire array. |
| **MyLinkedList** | `get(i)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Best case at head/tail; traversal requires stepping through $i$ pointers. |
| | `add(x)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | Append at tail updates next and prev references in constant time. |
| | `add(i, x)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Constant time at head/tail; requires $O(n)$ pointer traversal to find index. |
| | `remove(i)` | $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Constant time at head/tail; requires $O(n)$ pointer traversal in middle. |
| | `contains(x)`| $\Theta(1)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Best case at head; worst case requires full traversal to end. |
| **MinHeap** | `peekMin()` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | $\Theta(1)$ | Minimum element is strictly located at root index 0. |
| | `insert(x)` | $\Theta(1)$ | $\Theta(1)$ | $\Theta(\log n)$| $\Theta(1)$ | Appends at bottom; bubbles up at most $\lfloor \log_2 n \rfloor$ levels. |
| | `extractMin()`| $\Theta(\log n)$| $\Theta(\log n)$| $\Theta(\log n)$| $\Theta(1)$ | Moves last element to root and performs bubble-down through tree height. |
| | `buildHeap()` | $\Theta(n)$ | $\Theta(n)$ | $\Theta(n)$ | $\Theta(1)$ | Floyd's bottom-up sift-down satisfies $\sum \frac{h}{2^h} = O(1)$. |

---

## 2. Loop Invariant Proofs

### Proof 1: `DynamicArray.contains(int element)`
* **Invariant:** At the start of iteration $i$, for all $0 \le k < i$, `data[k] != element`.
* **Initialization:** Before the first iteration, $i = 0$. The range $0 \le k < 0$ is empty, so the statement holds vacuously.
* **Maintenance:** If the invariant holds before iteration $i$:
    - If `data[i] == element`, the method terminates and returns `true`.
    - If `data[i] != element`, the non-matching range extends to $0 \le k < i + 1$. Incrementing $i$ preserves the invariant for iteration $i+1$.
* **Termination:** The loop terminates either when `data[i] == element` is found, or when $i = size$. When $i = size$, no element matches within $0 \le k < size$, and the method returns `false`.
* **Conclusion:** This proves that `contains` returns `true` if and only if the element exists in the array.

### Proof 2: `MinHeap.bubbleDown(int index)`
* **Invariant:** Before each iteration, every node in the heap satisfies the min-heap property $A[\text{parent}] \le A[\text{child}]$, except possibly the node at $index$ and its direct children.
* **Initialization:** Prior to the loop, the root was replaced by the last leaf element, so any potential heap violation is isolated strictly to $index = 0$.
* **Maintenance:** In iteration $index$, if $A[index]$ is greater than its smallest child $c$, swapping them restores the heap property at $index$. The potential violation moves solely to child $c$. Setting $index = c$ preserves the invariant before the next iteration.
* **Termination:** The loop terminates when $index$ has no children or when $A[index] \le \min(A[left], A[right])$. At termination, no violations remain.
* **Conclusion:** This guarantees that upon completion, the min-heap order invariant holds across all nodes.

---

## 3. Discussion & Performance Analysis

1. DynamicArray significantly outperforms MyLinkedList on random access (W1) and sequential traversal due to CPU cache spatial locality.
2. Contiguous primitive memory in DynamicArray enables whole cache lines (64 bytes) to be prefetched into L1/L2 cache, executing sequential lookups with negligible latency.
3. MyLinkedList experiences pointer chasing overhead: nodes are independently allocated in heap memory, causing frequent CPU cache misses.
4. Each MyLinkedList node carries substantial memory overhead (object header, next/prev pointers) for storing a single 4-byte int, triggering Garbage Collector activity.
5. In Workload 3 (head variant), MyLinkedList demonstrates superior performance since node linking at boundaries is true $O(1)$ without element shifts.
6. When inserting into the middle, DynamicArray remains competitive despite $O(n)$ shifts because array shifting is highly optimized at the hardware cache line level compared to node traversal.
7. MinHeap provides efficient priority extraction with $O(\log n)$ complexity while keeping memory localized within a single array.
8. DynamicArray is the best choice for random access and append-heavy tasks, MyLinkedList is ideal for frequent boundary insertions/deletions, and MinHeap is optimal for priority queue scheduling.