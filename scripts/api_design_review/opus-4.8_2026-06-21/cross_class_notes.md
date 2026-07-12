# Cross-class consistency notes (verified)

## Tuple family (8 mirrors + PrimitiveTuple base)
- copyOf(Xxx[]) declares generic self-type return `<TP extends XxxTuple<TP>> TP`; runtime type chosen by length => CCE footgun. Identical in all 8. (IntTuple.java:376)
- Arity-specific accept/map/filter exist only for Tuple2/Tuple3 (Bi/Tri); 4..9 absent. All 8.
- No arity-agnostic indexed accessor get(int)/elementAt(int); only public fields _1.._9 + arity(). All 8.
- Empty-tuple aggregate contract: sum()->0 (identity), min/max/median/average -> NoSuchElementException. Documented, identical across the 7 numeric tuples. (IntTuple0 920-967)
- Null callback: forEach uses N.checkArgNotNull -> IllegalArgumentException (IntTuple.java:754); accept/map/filter (PrimitiveTuple 56/73/90) deref -> NullPointerException. Systemic IAE-vs-NPE split across whole family.
- of(8)/of(9) @Deprecated in all 8 with rationale "use a custom class with meaningful property names". Intentional+consistent (healthy).
- OUTLIER: LongTuple has a concrete base-class toString() at LongTuple.java:956; the other 7 tuple bases have none (each concrete TupleN overrides). Dead/redundant.
- BooleanTuple: no scalar reduction at all (no count of true). Only non-numeric variant.
- Double/Float aggregate vs equality NaN/-0.0 semantics: in-scope source uses N.min/N.max/N.median (aggregates) and N.equals (contains/equals) UNIFORMLY — any IEEE-vs-compare split lives in external N (abacus-common), NOT verifiable here. Low-confidence note only.
- Tuple1.hashCode returns the per-type canonical hash (IntTuple1 -> _1 == Integer.hashCode; Long/Float/Double use X.hashCode). CONSISTENT — not a finding.

## Arrays family (sealed Arrays + nested f/ff/fff; per-type template blocks)
- Null-handling inconsistency: inferred-element-type generic overloads (reshape T[]/T[][], flatten T[][]/T[][][], map UnaryOperator, zip no-target) call N.checkArgNotNull(a) -> IAE on null `a` (20542/20590/22333/22387/20713/22515/21196/21435/23032/23277). ALL primitive variants + targetElementType generic overloads + mutateFlattened (checks action, not a: 20651/22451) treat null/empty as empty-result/no-op. Inconsistent contract for the same family.
- Dimensional/coverage gaps in the OBJECT namespace: class `f` (1D object, 19903-20394) has only map(Function,Class)+mapToX — missing updateAll/replaceIf/map(UnaryOperator)/zip that exist for all primitives 1D and for objects 2D(ff)/3D(fff).
- minImmediateSubArrayLength/maxImmediateSubArrayLength only 2D (all primitives + ff Object[][]); none 3D (fff). elementCount has both 2D(ff 22018)+3D(fff 23545). Asymmetry vs elementCount.
- Conversion matrix toX(srcType[]) incomplete/asymmetric: char widens only to int (no char->long/float/double); long->int missing though int->long present; double->float missing though float->double + double->int/long present.
- ff/fff object println are package-private (22134/23591); public entry = parent Arrays.println (148/202, delegate). Javadoc examples show inaccessible `ff.println(...)`.
- println bundles I/O (System.out via N.println) with formatting; no pure toString/format variant.
- Cosmetic: int 3D zip 3-arg+defaults at 10835 omits the `if(minLen<maxLen)` guard used by 2D/generic.

## ImmutableIntArray
- average() returns 0D on empty (397) while min/max throw NoSuchElementException (282/320) AND IntTuple.average throws AND IntStream.average returns OptionalDouble => inconsistent 3 ways.
- Factories: only unsafeWrap/copyOf; no of(int...); no copyOf(int[],from,to). No toArray()/median()/first()/last()/indexOf()/iterator()/toList(). IntTuple sibling has toArray/median.
- get(int) leaks raw ArrayIndexOutOfBoundsException (433, explicit no-check comment) while copyOfRange uses N.checkFromToIndex -> IndexOutOfBoundsException (619).
- length() (array convention) vs library size() (list/tuple). Lone @Beta class with no primitive siblings.

## Points
- Curated matrix: coord {Byte,Int,Long,Double} x value {Byte,Int,Long,Double,Obj}, D2+D3, 20 records each, all uniform with of(). Doc explicitly says "common coordinate widths". Omits Short/Char/Float (present in Tuple family). Excellent internal symmetry; gap is documented/intentional.
