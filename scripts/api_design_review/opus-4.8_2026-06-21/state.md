# API Design Review — State

## Decisions (fixed once; reuse on resume)
- MODEL = opus-4.8
- DATE = 2026-06-21
- EXECUTION = PARALLEL (one sub-agent per class/family batch; orchestrator VERIFIES every finding against source before it enters a report)
- Output folder = scripts/api_design_review/opus-4.8_2026-06-21/

## In-scope classes (12) — package com.landawn.abacus.util, ./src/main/java
| Class | Lines | Kind |
|---|---|---|
| PrimitiveTuple.java | 103 | abstract sealed base for tuple families |
| BooleanTuple.java | 3420 | abstract sealed; nested Tuple0..9 (non-numeric) |
| ByteTuple.java | 4759 | abstract sealed; nested Tuple0..9 (numeric) |
| CharTuple.java | 4871 | abstract sealed; nested Tuple0..9 (numeric) |
| ShortTuple.java | 4531 | abstract sealed; nested Tuple0..9 (numeric) |
| IntTuple.java | 4343 | abstract sealed; nested Tuple0..9 (numeric) |
| LongTuple.java | 4557 | abstract sealed; nested Tuple0..9 (numeric) |
| FloatTuple.java | 4753 | abstract sealed; nested Tuple0..9 (numeric, float) |
| DoubleTuple.java | 4554 | abstract sealed; nested Tuple0..9 (numeric, float) |
| ImmutableIntArray.java | 743 | final; implements Immutable |
| Points.java | 2335 | final; nested D2/D3 holding coordinate-value records |
| Arrays.java | 23656 | sealed; permits f; nested f/ff/fff; 2D/3D array utils |

## Relationship map
- PrimitiveTuple<TP> = abstract sealed base. permits BooleanTuple, ByteTuple, CharTuple, DoubleTuple, FloatTuple, IntTuple, LongTuple, ShortTuple.
  Provides: arity() [abstract], accept(), map(), filter(), toOptional().
- Each XxxTuple<TP> extends PrimitiveTuple<TP>, abstract sealed, permits XxxTuple0..XxxTuple9 (arity-specific final classes).
  Common API (all 8): of(1..9 args), arity(), reverse(), contains(), forEach(), map(2), filter(2), accept(2), toList(), toArray(), stream(), copyOf(), toString/hashCode/equals.
  Numeric API (7: Byte/Char/Short/Int/Long/Float/Double): + sum(), min(), max(), median(), average().
  BooleanTuple correctly OMITS numeric aggregates. ==> The 8 tuple classes are INTENTIONAL MIRRORS. Cross-variant consistency is the primary review axis; a method shared via mirroring is NOT a duplicate.
- Arrays = sealed; permits Arrays.f. Nested public final classes: f (extends Arrays), ff, fff. Main Arrays class = static 2D/3D array utilities organized as REPEATING PER-TYPE BLOCKS (boolean/char/byte/short/int/long/float/double/generic), each block = {println, updateAll, replaceIf, reshape, flatten, mutateFlattened, zip, elementCount, minImmediateSubArrayLength, maxImmediateSubArrayLength}. Separate toX conversion block (16449-19902). f/ff/fff = functional 1D/2D/3D helpers (map/mapToX/zip/updateAll/etc.). Arrays template-mirrored ==> cross-type consistency is primary axis.
- ImmutableIntArray = standalone immutable int[] wrapper. unsafeWrap/copyOf factories; isEmpty/length/contains/min/max/sum/average/get/forEach/forEachIndexed/stream/copyOfRange + value semantics. (No sibling Immutable{Long,Double}Array in this package's scope.)
- Points = namespace class. D2 (2D) + D3 (3D), each containing a matrix of record types: coordinate-type {Byte,Int,Long,Double} x value-type {Byte,Int,Long,Double,Obj<T>}. Each record has an of() factory.

## Dispatch plan (PARALLEL) + status
| Agent | Scope | Status |
|---|---|---|
| T1 | PrimitiveTuple + IntTuple (canonical numeric) | done |
| T2 | BooleanTuple + DoubleTuple + FloatTuple + CharTuple + cross-tuple structural check (Byte/Short/Long) | done |
| AR1 | Arrays: mapToObj/mapToX, map, updateAll, replaceIf (transforms) across types + f/ff/fff | done |
| AR2 | Arrays: reshape, flatten, mutateFlattened, elementCount, min/maxImmediateSubArrayLength, println (structural) | done |
| AR3 | Arrays: zip family + toBoolean/toChar/toByte/toShort/toInt/toLong/toFloat/toDouble conversions | done |
| P1 | ImmutableIntArray + Points | done |

## Verification status (orchestrator must verify each candidate finding against source)
- T1 verified: yes
- T2 verified: yes
- AR1 verified: yes
- AR2 verified: yes
- AR3 verified: yes
- P1 verified: yes

## Reports written
- PrimitiveTuple.txt, IntTuple.txt, BooleanTuple.txt, ByteTuple.txt, CharTuple.txt, ShortTuple.txt, LongTuple.txt, FloatTuple.txt, DoubleTuple.txt, ImmutableIntArray.txt, Points.txt, Arrays.txt, SUMMARY.txt
