# Javadoc Consistency Analysis

## Summary

This document analyzes javadoc inconsistencies found in similar methods across Matrix and Tuple classes.

## Matrix Classes - Diagonal Methods Inconsistencies

### 1. getLU2RD() - Main Diagonal Getter

**Inconsistencies Found:**

| Class | Description Style | Consistency Issues |
|-------|-------------------|-------------------|
| Boolean Matrix | "Retrieves the elements on the main diagonal..." + "This method extracts the main diagonal elements at positions (0,0), (1,1), (2,2), etc." | Uses "Retrieves" instead of "Returns", has extra detail paragraph |
| ByteMatrix | "Returns the elements on the main diagonal from left-upper to right-down. The matrix must be square..." | Good, concise |
| CharMatrix | "Returns the elements on the main diagonal from left-upper to right-down. The matrix must be square..." | Good, concise |
| DoubleMatrix | "Returns the elements on the main diagonal from left-upper to right-down. The matrix must be square..." | Good, concise |
| ShortMatrix | Similar to above | Good, concise |

**Recommended Standard:**
```java
/**
 * Returns the elements on the main diagonal from left-upper to right-down.
 * The matrix must be square (rows == columns) for this operation.
 *
 * <p>This method extracts the main diagonal elements at positions (0,0), (1,1), (2,2), etc.
 *
 * <p>Example:
 * <pre>{@code
 * {Type}Matrix matrix = {Type}Matrix.of(new {type}[][]{{...}});
 * {type}[] diagonal = matrix.getLU2RD(); // Returns [...]
 * }</pre>
 *
 * @return a new {type} array containing the main diagonal elements
 * @throws IllegalStateException if the matrix is not square (rows != columns)
 */
```

### 2. getRU2LD() - Anti-Diagonal Getter

**Inconsistencies Found:**

| Class | Description Style | Consistency Issues |
|-------|-------------------|-------------------|
| BooleanMatrix | "Retrieves the anti-diagonal elements..." + "This method extracts the anti-diagonal (secondary diagonal) elements from top-right to bottom-left, at positions (0,n-1), (1,n-2), (2,n-3), etc." | Extra detail, different verb |
| ByteMatrix | "Returns the elements on the anti-diagonal from right-upper to left-down..." | Good, concise |
| CharMatrix | "Returns a copy of the elements on the anti-diagonal (right-upper to left-down)..." | Says "copy" (redundant) |
| DoubleMatrix | "Returns the elements on the anti-diagonal from right-upper to left-down..." | Good, concise |

**Recommended Standard:**
```java
/**
 * Returns the elements on the anti-diagonal from right-upper to left-down.
 * The matrix must be square (rows == columns) for this operation.
 *
 * <p>This method extracts the anti-diagonal (secondary diagonal) elements from
 * top-right to bottom-left, at positions (0,n-1), (1,n-2), (2,n-3), etc.
 *
 * <p>Example:
 * <pre>{@code
 * {Type}Matrix matrix = {Type}Matrix.of(new {type}[][]{{...}});
 * {type}[] diagonal = matrix.getRU2LD(); // Returns [...]
 * }</pre>
 *
 * @return a new {type} array containing the anti-diagonal elements
 * @throws IllegalStateException if the matrix is not square (rows != columns)
 */
```

### 3. setLU2RD() - Main Diagonal Setter

**Inconsistencies Found:**

| Class | Description Style | Consistency Issues |
|-------|-------------------|-------------------|
| BooleanMatrix | "Sets the diagonal elements from left-up to right-down (main diagonal)..." | Slightly different wording |
| CharMatrix | "Sets the elements on the main diagonal (left-upper to right-down)..." | Different word order, uses "left-upper" instead of "left-up" |
| ShortMatrix | "Sets the values on the main diagonal (left-up to right-down)..." | Uses "values" instead of "elements" |

**Exception Documentation Inconsistencies:**
- Some say "@throws IllegalStateException if the matrix is not square (rows != columns)"
- Some say "@throws IllegalStateException if the matrix is not square (rows != cols)"

**Recommended Standard:**
```java
/**
 * Sets the elements on the main diagonal from left-up to right-down (main diagonal).
 * The matrix must be square (rows == columns), and the diagonal array must have
 * at least as many elements as the matrix has rows.
 *
 * <p>This method sets the main diagonal elements at positions (0,0), (1,1), (2,2), etc.
 * If the diagonal array is longer than needed, extra elements are ignored.
 *
 * <p>Example:
 * <pre>{@code
 * {Type}Matrix matrix = {Type}Matrix.of(new {type}[][]{{...}});
 * matrix.setLU2RD(new {type}[]{...});
 * // Diagonal is now [...]
 * }</pre>
 *
 * @param diagonal the new values for the main diagonal; must have length &gt;= rows
 * @throws IllegalStateException if the matrix is not square (rows != columns)
 * @throws IllegalArgumentException if diagonal array length &lt; rows
 */
```

### 4. setRU2LD() - Anti-Diagonal Setter

**Inconsistencies Found:**

Similar to setLU2RD(), with variations in:
- "Sets the diagonal elements..." vs "Sets the elements..." vs "Sets the values..."
- "right-up to left-down" vs "right-upper to left-down"
- "anti-diagonal" placement differences

**Recommended Standard:**
```java
/**
 * Sets the elements on the anti-diagonal from right-up to left-down (anti-diagonal).
 * The matrix must be square (rows == columns), and the diagonal array must have
 * at least as many elements as the matrix has rows.
 *
 * <p>This method sets the anti-diagonal (secondary diagonal) elements from
 * top-right to bottom-left, at positions (0,n-1), (1,n-2), (2,n-3), etc.
 * If the diagonal array is longer than needed, extra elements are ignored.
 *
 * <p>Example:
 * <pre>{@code
 * {Type}Matrix matrix = {Type}Matrix.of(new {type}[][]{{...}});
 * matrix.setRU2LD(new {type}[]{...});
 * // Anti-diagonal is now [...]
 * }</pre>
 *
 * @param diagonal the new values for the anti-diagonal; must have length &gt;= rows
 * @throws IllegalStateException if the matrix is not square (rows != columns)
 * @throws IllegalArgumentException if diagonal array length &lt; rows
 */
```

## Key Inconsistencies Across All Methods

1. **Verb Choice**: "Returns" vs "Retrieves" vs "Gets"
2. **Noun Choice**: "elements" vs "values"
3. **Direction Description**: "left-upper" vs "left-up", "right-upper" vs "right-up"
4. **Exception Messages**: "rows != columns" vs "rows != cols"
5. **Redundant Words**: Some say "Returns a copy" when "Returns" is sufficient
6. **Paragraph Structure**: Some methods have extra detail paragraphs, others don't
7. **Example Formatting**: Minor differences in example formatting and comments

## Harmonization Strategy

To harmonize the javadoc:

1. **Use consistent verbs**: "Returns" for getters, "Sets" for setters
2. **Use consistent nouns**: "elements" throughout
3. **Use consistent direction terms**: "left-upper", "right-upper", "left-down", "right-down"
4. **Use consistent exception messages**: Always use "rows != columns" (not abbreviated)
5. **Remove redundant words**: Don't say "returns a copy", just "returns"
6. **Consistent structure**:
   - Main description (1-2 sentences)
   - Detail paragraph explaining what the method does
   - Example with code
   - @param, @return, @throws tags
7. **Consistent example style**: Use similar matrix examples across all types

## Implementation Priority

**High Priority** (Core methods that must be harmonized):
1. getLU2RD() and setLU2RD() - Main diagonal operations
2. getRU2LD() and setRU2LD() - Anti-diagonal operations
3. get(int, int) and set(int, int, value) - Element access
4. row() and setRow() - Row operations
5. column() and setColumn() - Column operations

**Medium Priority**:
1. Navigation methods: upOf(), downOf(), leftOf(), rightOf()
2. Transformation methods: transpose(), rotate90(), rotate180(), rotate270()
3. Factory methods: empty(), of(), random(), repeat()

**Lower Priority**:
1. Stream methods
2. Specialized operations

## Next Steps

1. Apply harmonized javadoc to all Matrix diagonal methods (getLU2RD, getRU2LD, setLU2RD, setRU2LD)
2. Verify consistency across all 8 primitive matrix types
3. Extend to other similar methods
4. Create unit tests to verify javadoc examples compile and work correctly
