package calculator;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static Map<String, Long> vars = new HashMap<>();
    static List<String> members;
    static List<String> signs;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine().replaceAll("\\s+", "");
            TypeOfLine type = lineTypeAnalyze(line);

            if (type == TypeOfLine.COMMAND) {
                if ("/help".equals(line)) {
                    System.out.println("The program adds an subtracts the numbers and variables.");
                }   else if ("/exit".equals(line)) {
                    System.out.println("Bye!");
                    break;
                }   else {
                    System.out.println("Unknown command");
                }
            }

            if (type == TypeOfLine.VAR_INIT) {
                var variable = line.split("=");
                if (line.matches("[A-Za-z]+=[A-Za-z]+")) {
                    if (vars.containsKey(variable[1])) {
                        vars.put(variable[0], vars.get(variable[1]));
                    }   else {
                        System.out.println("Unknown variable");
                    }
                }   else {
                    vars.put(variable[0], Long.valueOf(variable[1]));
                }


            }   else if (type == TypeOfLine.INVALID_IDENTIFIER) {
                System.out.println("Invalid identifier");
            }   else if (type == TypeOfLine.INVALID_ASSIGNMENT) {
                System.out.println("Invalid assignment");
            }

            if (type == TypeOfLine.SINGLE_VAR) {
                System.out.println(vars.containsKey(line) ? vars.get(line) : "Unknown variable");
            }

            if (type == TypeOfLine.EXPRESSION) {
                members = Arrays.stream(line.split("[-+]"))
                        .filter(x -> !"".equals(x))
                        .collect(Collectors.toList());

                signs = Arrays.stream(line.split("[A-Za-z\\d]+"))
                        .map(x -> x.chars().filter(c -> c == '-').count() % 2 == 1 ? "-" : "+")
                        .collect(Collectors.toList());

                long result = 0;
                for (int i = 0; i < signs.size(); i++) {
                    result = "-".equals(signs.get(i)) ?
                            result - getValueOfMember(i) : result + getValueOfMember(i);
                }
                System.out.println(result);
            }

            if (type == TypeOfLine.INVALID_LINE) {
                System.out.println("Invalid variable");
            }
        }
    }

    static TypeOfLine lineTypeAnalyze(String line) {
        if ("".equals(line)) {
            return TypeOfLine.EMPTY_LINE;
        }   else if (line.matches("[A-Za-z]+=(\\d+|[A-Za-z]+)")) {
            return TypeOfLine.VAR_INIT;
        }   else if (line.matches("[A-Za-z]+")) {
            return TypeOfLine.SINGLE_VAR;
        }   else if (line.matches("/[A-Za-z\\d]+")) {
            return TypeOfLine.COMMAND;
        }   else if (line.matches("\\d*[A-Za-z]+\\d*=\\d+")) {
            return TypeOfLine.INVALID_IDENTIFIER;
        }   else if (line.matches("[A-Za-z]+=[A-Za-z]*\\d+[A-Za-z]*" )) {
            return TypeOfLine.INVALID_ASSIGNMENT;
        }   else if (line.matches("[A-Za-z]+=\\d+=\\d+")) {
            return TypeOfLine.INVALID_ASSIGNMENT;
        }   else if (line.matches("([-+*/]*([A-Za-z]+|\\d+))?([-+*/]+([A-Za-z]+|\\d+))*")) {
            return TypeOfLine.EXPRESSION;
        }   else return TypeOfLine.INVALID_LINE;
    }

    static long getValueOfMember(int indexOfMember) {
        return vars.containsKey(members.get(indexOfMember)) ?
                vars.get(members.get(indexOfMember)) :
                Long.parseLong(members.get(indexOfMember));
    }
}

enum TypeOfLine {
    COMMAND,
    EMPTY_LINE,
    VAR_INIT,
    SINGLE_VAR,
    EXPRESSION,
    INVALID_LINE,
    INVALID_IDENTIFIER,
    INVALID_ASSIGNMENT
}

