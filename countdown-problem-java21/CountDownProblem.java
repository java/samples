import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Stream;

/*
 * This program is Java port of the Haskell example at
 * https://www.cs.nott.ac.uk/~pszgmh/pgp-countdown.hs
 *
 * The problem and the solution approaches are explained
 * in Prof. Graham Hutton's youtube video at
 * https://youtu.be/CiXDS3bBBUo?list=PLF1Z-APd9zK7usPMx3LGMZEHrECUGodd3
 *
 * This Java program requires JDK 21+
 */
class CountDownProblem {

   // data Op = Add | Sub | Mul | Div
   enum Op {
       Add, Sub, Mul, Div;

       // instance show Op
       @Override
       public String toString() {
          return switch (this) {
             case Add -> "+";
             case Sub -> "-";
             case Mul -> "*";
             case Div -> "/";
          };
       }
   }

   // cache enum value array
   static final Op[] operators = Op.values();

   // valid' :: Op -> Int -> Int -> Bool
   static boolean isValid(Op op, int x, int y) {
      return switch (op) {
         case Add -> x <= y;
         case Sub -> x > y;
         case Mul -> x != 1 && y != 1 && x <= y;
         case Div -> y != 1 && x % y == 0;
      };
   }

   // apply :: Op -> Int -> Int -> Int
   static int apply(Op op, int x, int y) {
      return switch (op) {
         case Add -> x + y;
         case Sub -> x - y;
         case Mul -> x * y;
         case Div -> x / y;
      };
   }

   // data Expr = Val Int | App Op Expr Expr
   sealed interface Expr {
      // brak helper for instance Show Expr
      static String brak(Expr expr) {
         return switch (expr) {
            // brak (Val n) = show n
            case Val(var n) -> Integer.toString(n);

            // brak e       = "(" ++ show e ++ ")"
            default -> "(" + toStr(expr) + ")";
         };
      }

      // instance Show Expr
      static String toStr(Expr expr) {
         return switch (expr) {
            // show (Val n)     = show n
            case Val(var n) -> Integer.toString(n);

            // show (App o l r) = brak l ++ show o ++ brak r
            //          where
            //             brak (Val n) = show n
            //             brak e       = "(" ++ show e ++ ")"
            case App(var op, var l, var r) -> brak(l) + op + brak(r);
         };
      }
   }

   record Val(int v) implements Expr {
      // instance Show Expr
      @Override
      public String toString() {
         return Expr.toStr(this);
      }
   }

   record App(Op op, Expr l, Expr r) implements Expr {
      // instance Show Expr
      @Override
      public String toString() {
         return Expr.toStr(this);
      }
   }

   // eval :: Expr -> [Int]
   // Using OptionalInt instead of List<Integer>
   static OptionalInt eval(Expr expr) {
      return switch (expr) {
         // eval (Val n)     = [n | n > 0]
         case Val(var n) -> n > 0 ? OptionalInt.of(n) : OptionalInt.empty();


         // eval (App o l r) = [apply o x y | x <- eval l,
         //                                   y <- eval r,
         //                                   valid o x y]
         case App(var op, var l, var r) -> {
            var x = eval(l);
            var y = eval(r);
            yield (x.isPresent() && y.isPresent() &&
	           isValid(op, x.getAsInt(), y.getAsInt())) ?
	       OptionalInt.of(apply(op, x.getAsInt(), y.getAsInt())) :
	       OptionalInt.empty();
         }
      };
   }

   // type Result = (Expr,Int)
   record Result(Expr expr, int value) {
      @Override
      public String toString() {
         return expr.toString() + " = " + value;
      }
   } 

   // combine'' :: Result -> Result -> [Result]
   static List<Result> combine(Result lx, Result ry) {
      // (l,x), (r,y) pattern
      var l = lx.expr();
      var x = lx.value();
      var r = ry.expr();
      var y = ry.value();

      // combine'' (l,x) (r,y) = [(App o l r, apply o x y) | o <- ops, valid' o x y]
      return Stream.of(operators).
                filter(op -> isValid(op, x, y)).
                map(op -> new Result(new App(op, l, r), apply(op, x, y))).
                toList();
   }

   // results' :: [Int] -> [Result]
   static List<Result> results(List<Integer> ns) {
      // results' []  = []                 
      if (ns.isEmpty()) {
         return List.of();
      }

      // results' [n] = [(Val n,n) | n > 0]
      if (ns.size() == 1) {
         var n = head(ns);
         return n > 0 ? List.of(new Result(new Val(n), n)) : List.of();
      }

      // results' ns  = [res | (ls,rs) <- split ns,
      //                 lx     <- results' ls,
      //                 ry     <- results' rs,
      //                 res    <- combine'' lx ry]
      var res = new ArrayList<Result>();

      // all possible non-empty splits of the input list
      // split :: [a] -> [([a],[a])] equivalent for-loop
      for (int i = 1; i < ns.size(); i++) {
         var ls = ns.subList(0, i);
         var rs = ns.subList(i, ns.size());
         var lxs = results(ls);
         var rys = results(rs);
         for (Result lx : lxs) {
            for (Result ry : rys) {
               res.addAll(combine(lx, ry));
            }
         }
      } 
      return res;
   }

   // List utilities
   // : operator
   static <T> List<T> cons(T head, List<T> tail) {
      final var tailLen = tail.size();
      return switch (tailLen) {
          case 0 -> List.of(head);
          case 1 -> List.of(head, tail.get(0));
          case 2 -> List.of(head, tail.get(0), tail.get(1));
          case 3 -> List.of(head, tail.get(0), tail.get(1), tail.get(2));
          default -> {
             var res = new ArrayList<T>(1 + tailLen);
             res.add(head);
             res.addAll(tail);
             yield res;
          }
      };
   }

   static <T> T head(List<T> list) {
      return list.get(0);
   }

   static <T> List<T> tail(List<T> list) {
      final var len = list.size();
      return len == 1 ? List.of() : list.subList(1, len);
   }

   // subs :: [a] -> [[a]]
   static List<List<Integer>> subs(List<Integer> ns) {
      // subs [] = [[]]
      if (ns.isEmpty()) {
         return List.of(List.of());
      }

      // subs (x:xs)
      var x = head(ns);
      var xs = tail(ns);

      // where yss = sub(xs)
      var yss = subs(xs);

      // yss ++ map (x:) yss
      var res = new ArrayList<List<Integer>>();
      res.addAll(yss);
      yss.stream().
          map(l -> cons(x, l)).
          forEach(res::add);

      return res;
   }

   // interleave :: a -> [a] -> [[a]]
   // Using Stream<List<Integer> instead of List<List<Integer>>
   static Stream<List<Integer>> interleave(int x, List<Integer> ns) {
      // interleave x []     = [[x]]
      if (ns.isEmpty()) {
         return Stream.of(List.of(x));
      }

      // interleave x (y:ys)
      var y = head(ns);
      var ys = tail(ns);

      // outer : translated as Stream.concat
      // (x:y:ys) : map (y:) (interleave x ys)
      return Stream.concat(
         // x:y:ys == x:ns
         Stream.of(cons(x, ns)),
         // map (y:) (interleave x ys)
         interleave(x, ys).map(l -> cons(y, l))
      );
   }

   // perms :: [a] -> [[a]]
   // Using Stream<List<Integer> instead of List<List<Integer>>
   static Stream<List<Integer>> perms(List<Integer> ns) {
      // perms []     = [[]] 
      if (ns.isEmpty()) {
         return Stream.of(List.of());
      }

      // perms (x:xs)
      var x = head(ns);
      var xs = tail(ns);

      // concat (map ...) is translated as flatMap
      // concat (map (interleave x) (perms xs))
      return perms(xs).flatMap(l -> interleave(x, l));
   }

   // choices :: [a] -> [[a]]
   // Using Stream<List<Integer> instead of List<List<Integer>>
   static Stream<List<Integer>> choices(List<Integer> ns) {
      // concat . map is translated as flatMap
      // choices = concat . map perms . subs
      return subs(ns).stream().flatMap(CountDownProblem::perms);
   }

   // solutions'' :: [Int] -> Int -> [Expr]
   // Using Stream<Expr> instead of List<Expr> 
   static Stream<Expr> solutions(List<Integer> ns, int n) {
      // solutions'' ns n = [e | ns' <- choices ns, (e,m) <- results' ns', m == n]
      return choices(ns).
         flatMap(choice -> results(choice).stream()).
         filter(res -> res.value() == n).
         map(Result::expr);
   }

   /*
    * usage example:
    * 
    *    java CountDownProblem.java 1,3,7,10,25,50 765
    */
   public static void main(String[] args) {
      if (args.length != 2) {
         System.err.println("usage: java CountDownProblem.java <comma-separated-values> <target>");
         System.exit(1);
      }

      int target = Integer.parseInt(args[1]);
      List<Integer> numbers = Stream.of(args[0].split(",")).map(Integer::parseInt).toList();
      // uniqueness check
      try {
         Set.of(numbers.toArray());
      } catch (IllegalArgumentException iae) {
         System.err.println(iae);
         System.exit(2);
      }

      var start = System.currentTimeMillis();
      solutions(numbers, target).forEach(e -> {
         System.out.println(e);
      });
      System.out.println("Time taken (ms): " + (System.currentTimeMillis() - start));
   }
}
