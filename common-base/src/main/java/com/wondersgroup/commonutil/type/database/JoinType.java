package com.wondersgroup.commonutil.type.database;

import com.wondersgroup.commonutil.type.CommonType;

public enum JoinType implements CommonType {
	COMMA(","), //
    JOIN("JOIN"), //
    INNER_JOIN("INNER JOIN"), //
    CROSS_JOIN("CROSS JOIN"), //
    NATURAL_JOIN("NATURAL JOIN"), //
    NATURAL_CROSS_JOIN("NATURAL CROSS JOIN"), //
    NATURAL_LEFT_JOIN("NATURAL LEFT JOIN"), //
    NATURAL_RIGHT_JOIN("NATURAL RIGHT JOIN"), //
    NATURAL_INNER_JOIN("NATURAL INNER JOIN"), //
    LEFT_OUTER_JOIN("LEFT JOIN"), //
    LEFT_SEMI_JOIN("LEFT SEMI JOIN"), //
    LEFT_ANTI_JOIN("LEFT ANTI JOIN"), //
    RIGHT_OUTER_JOIN("RIGHT JOIN"), //
    FULL_OUTER_JOIN("FULL JOIN"),//
    STRAIGHT_JOIN("STRAIGHT_JOIN"), //
    OUTER_APPLY("OUTER APPLY"),//
    CROSS_APPLY("CROSS APPLY");

    public final String name;
    public final String name_lcase;

    JoinType(String name){
        this.name = name;
        this.name_lcase = name.toLowerCase();
    }

    public static String toString(JoinType joinType) {
        return joinType.name;
    }

	@Override
	public String getTypeName() {
		return "JoinType";
	}
	
	/**
	 
	public boolean contains(SQLTableSource tableSource, SQLExpr condition) {
        if (right.equals(tableSource)) {
            if (this.condition == condition) {
                return true;
            }

            return this.condition != null && this.condition.equals(condition);
        }

        if (left instanceof SQLJoinTableSource) {
            SQLJoinTableSource joinLeft = (SQLJoinTableSource) left;

            if (tableSource instanceof SQLJoinTableSource) {
                SQLJoinTableSource join = (SQLJoinTableSource) tableSource;

                if (join.right.equals(right) && this.condition.equals(condition) && joinLeft.right.equals(join.left)) {
                    return true;
                }
            }

            return joinLeft.contains(tableSource, condition);
        }

        return false;
    }
    
	 */
	

}
