import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String line = in.nextLine().replaceAll("[ \t]", "");
        int n = Integer.parseInt(line);
        ArrayList<FunCustom> functions = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            line = in.nextLine().replaceAll("[ \t]", "");
            for (FunCustom funCustom : functions) {
                if (funCustom.hasFunc(line)) {
                    line = funCustom.replaceFunc(line);
                }
            }
            functions.add(new FunCustom(line));
        }
        line = in.nextLine().replaceAll("[ \t]", "");
        for (FunCustom function : functions) {
            line = function.replaceFunc(line);
        }
        ReadTokens reader = new ReadTokens(line);
        Parser parser = new Parser(reader);
        Expr expr = parser.parseExpr();
        String out = expr.toString();
        if (expr.isEmpty()) { System.out.print("0"); }
        else { System.out.print(out); }
    }
}
