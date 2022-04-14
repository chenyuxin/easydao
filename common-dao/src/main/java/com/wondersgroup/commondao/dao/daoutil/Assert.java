package com.wondersgroup.commondao.dao.daoutil;

/**
 * commonDao的断言<br>
 * 用于执行失败的dao在事务层抛出异常RuntimeException，回滚事务。
 */
@Deprecated
public class Assert {
	
	/**
	 * commonDao的断言<br>
	 * 用于执行失败的dao在事务层抛出异常RuntimeException，回滚事务。
	 * @param commonDaoResult commonDao返回的执行消息
	 */
	@Deprecated
	public static void isSuccessMessage(String commonDaoResult) {
		if(commonDaoResult.contains(DaoUtil.saveObj_FAILED_MESSAGE)
				|| commonDaoResult.contains(DaoUtil.updateObj_FAILED_MESSAGE)
				|| commonDaoResult.contains(DaoUtil.mergeObj_FAILED_MESSAGE)
				|| commonDaoResult.contains(DaoUtil.useTable_FAILED_MESSAGE)
				|| commonDaoResult.contains(DaoUtil.delObj_FAILED_MESSAGE)
				|| commonDaoResult.contains(DaoUtil.truncateTable_FAILED_MESSAGE)
				|| commonDaoResult.contains(DaoUtil.delDropTable_FAILED_MESSAGE)
		) {
			throw new RuntimeException(commonDaoResult);
		}
	}

}
