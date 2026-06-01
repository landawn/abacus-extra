#!/usr/bin/env node

const fs = require('fs');
const { replaceInActiveJavadocs } = require('./javadoc_region');

function replaceInFile(file, replacements) {
  let text = fs.readFileSync(file, 'utf8');
  const original = text;
  for (const [from, to] of replacements) {
    text = replaceInActiveJavadocs(text, from, to);
  }
  if (text !== original) fs.writeFileSync(file, text, 'utf8');
}

replaceInFile('src/main/java/com/landawn/abacus/util/Comparators.java', [
  ['List<Person> people = ...;', 'List<Person> people = new ArrayList<>();'],
  ['List<Product> products = ...;', 'List<Product> products = new ArrayList<>();'],
  ['List<Task> tasks = ...;', 'List<Task> tasks = new ArrayList<>();'],
  ['List<Grade> grades = ...;', 'List<Grade> grades = new ArrayList<>();'],
  ['List<Packet> packets = ...;', 'List<Packet> packets = new ArrayList<>();'],
  ['List<Product> inventory = ...;', 'List<Product> inventory = new ArrayList<>();'],
  ['List<File> files = ...;', 'List<File> files = new ArrayList<>();'],
  ['List<Measurement> measurements = ...;', 'List<Measurement> measurements = new ArrayList<>();'],
  ['List<Score> scores = ...;', 'List<Score> scores = new ArrayList<>();'],
  ['List<User> users = ...;', 'List<User> users = new ArrayList<>();'],
  ['Map<String, Integer> scores = ...;', 'Map<String, Integer> scores = new LinkedHashMap<>();'],
  ['List<Map.Entry<String, Integer>> entries = ...;', 'List<Map.Entry<String, Integer>> entries = new ArrayList<>();'],
]);

replaceInFile('src/main/java/com/landawn/abacus/util/Difference.java', [
  ['MapDifference<...>', 'MapDifference<?, ?, ?>'],
  ['BeanDifference<...>', 'BeanDifference<?, ?, ?>'],
]);

replaceInFile('src/main/java/com/landawn/abacus/util/Fnn.java', [
  ['Seq<Object, IOException> objects = ...;', 'Seq<Object, IOException> objects = Seq.of(new Object());'],
  ['Map<String, Integer> map = ...;', 'Map<String, Integer> map = new LinkedHashMap<>();'],
  ['Seq<Map.Entry<String, User>, IOException> entries = ...;', 'Seq<Map.Entry<String, User>, IOException> entries = Seq.of(new SimpleEntry<>("id", new User()));'],
  ['Map<String, Integer> original = ...;', 'Map<String, Integer> original = new LinkedHashMap<>();'],
  ['Seq<Entry<UserId, UserName>, Exception> entries = ...;', 'Seq<Entry<UserId, UserName>, Exception> entries = Seq.of(new SimpleEntry<>(new UserId(), new UserName()));'],
  ['List<String> keys = ...;', 'List<String> keys = Arrays.asList("a", "b");'],
  ['List<Integer> values = ...;', 'List<Integer> values = Arrays.asList(1, 2);'],
  ['Seq<String, Exception> names = ...;', 'Seq<String, Exception> names = Seq.of("Ann", "Bob");'],
  ['Seq<List<String>, Exception> seq = ...;', 'Seq<List<String>, Exception> seq = Seq.of(Arrays.asList("a", "b"));'],
]);

replaceInFile('src/main/java/com/landawn/abacus/util/IntList.java', [
  ['IntList squares = dataset.stream().map(x -> x * x).collect(...);',
    'IntList squares = dataset.stream()\n *     .map(x -> x * x)\n *     .collect(IntList::new, IntList::add, IntList::addAll);'],
]);

replaceInFile('src/main/java/com/landawn/abacus/util/Iterables.java', [
  ['*       ...\n     *       ImmutableList<B> tuple = ImmutableList.of(b0, b1, ...);',
    '*       for (B b2 : lists.get(2)) {\n     *         ImmutableList<B> tuple = ImmutableList.of(b0, b1, b2);\n     *       }'],
]);

replaceInFile('src/main/java/com/landawn/abacus/util/MoreExecutors.java', [
  ['ThreadPoolExecutor executor = new ThreadPoolExecutor(...);',
    'ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());'],
  ['ThreadPoolExecutor threadPool = new ThreadPoolExecutor(...);',
    'ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());'],
]);

replaceInFile('src/main/java/com/landawn/abacus/util/Seq.java', [
  ['seq.transform(s -> Seq.defer(() -> s.filter(...).map(...).someTerminalOperation(...)));',
    'Seq<Integer, RuntimeException> doubled = seq.transform(s -> Seq.defer(() -> s.filter(n -> n > 1).map(n -> n * 2)));'],
]);

replaceInFile('src/main/java/com/landawn/abacus/util/Fn.java', [
  ['Stream<List<String>> listStream = ...;', 'Stream<List<String>> listStream = Stream.of(Arrays.asList("a", "b"), Arrays.asList("c"));'],
]);
