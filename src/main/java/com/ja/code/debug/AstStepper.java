package com.ja.code.debug;

import com.github.javaparser.*;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.stmt.*;

import java.util.*;

public class AstStepper {

    private final List<Statement> steps = new ArrayList<>();
    private int pointer = 0;
    private final Map<String, Object> variables = new HashMap<>();

    public AstStepper(String code) {
        CompilationUnit cu = StaticJavaParser.parse(code);
        cu.findAll(Statement.class).forEach(steps::add);
    }

    public AstDebugState step() {

        if (pointer >= steps.size()) {
            return new AstDebugState(
                    -1,
                    "Execution finished",
                    variables,
                    true
            );
        }

        Statement st = steps.get(pointer++);
        evaluate(st);

        return new AstDebugState(
                st.getBegin().map(p -> p.line).orElse(-1),
                st.toString(),
                new HashMap<>(variables),
                false
        );
    }

    private void evaluate(Statement st) {

        if (st.isExpressionStmt()) {
            var expr = st.asExpressionStmt().getExpression();

            if (expr.isAssignExpr()) {
                var assign = expr.asAssignExpr();
                String var = assign.getTarget().toString();
                Object val = assign.getValue().toString();
                variables.put(var, val);
            }
        }
    }
}
