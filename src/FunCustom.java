import java.util.ArrayList;

public class FunCustom implements Function {
    private final String name;
    private final String body;
    private final ArrayList<String> varList = new ArrayList<>();

    public FunCustom(String str) {
        StringBuilder sb = new StringBuilder();
        int cur = 0;
        while (str.charAt(cur) != '\0') {
            if (str.charAt(cur) == '(') {
                cur++;
                break; }
            sb.append(str.charAt(cur));
            cur++;
        }
        this.name = sb.toString();
        sb.setLength(0);
        while (str.charAt(cur) != '\0') {
            if (str.charAt(cur) == ')') {
                cur++;
                varList.add(sb.toString());
                break; }
            else if (str.charAt(cur) == ',') {
                varList.add(sb.toString());
                sb.setLength(0);
            } else { sb.append(str.charAt(cur)); }
            cur++;
        }
        body = str.substring(cur + 1);
    }

    @Override
    public boolean hasFunc(String str) {
        return str.contains(name);
    }

    @Override
    public String replaceFunc(String string) {
        String str = string.replaceAll("exp", "#");
        while (hasFunc(str)) {
            int start = str.indexOf(name);
            int end = -1;
            int stack = -1;
            StringBuilder temp = new StringBuilder();
            temp.append("(");
            ArrayList<String> tempVarList = new ArrayList<>();
            for (int i = start + 2; i < str.length(); i++) {
                if (str.charAt(i) == ')') {
                    stack++;
                    temp.append(')');
                    if (stack == 0) {
                        tempVarList.add(temp.toString());
                        end = i;
                        break;
                    }
                } else if (str.charAt(i) == ',' && stack == -1) {
                    temp.append(')');
                    tempVarList.add(temp.toString());
                    temp.setLength(0);
                    temp.append('(');
                } else if (str.charAt(i) == '(') {
                    stack--;
                    temp.append("(");
                } else { temp.append(str.charAt(i)); }
            }
            StringBuilder sb = new StringBuilder(str);
            sb.replace(start, end + 1, substitution(tempVarList));
            str = sb.toString().replaceAll("exp", "#");
        }
        return str.replaceAll("#", "exp");
    }

    public String substitution(ArrayList<String> tempVarList) {
        String temp = "(" + body + ")";
        for (int i = 0; i < varList.size(); i++) {
            if (!varList.get(i).equals("x")) { continue; }
            temp = temp.replaceAll("exp", "#");
            temp = temp.replaceAll(varList.get(i), tempVarList.get(i));
        }
        for (int i = 0; i < varList.size(); i++) {
            if (varList.get(i).equals("x")) { continue; }
            temp = temp.replaceAll("exp", "#");
            temp = temp.replaceAll(varList.get(i), tempVarList.get(i));
        }
        return temp;
    }
}
