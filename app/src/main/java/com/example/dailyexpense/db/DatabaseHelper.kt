package com.example.dailyexpense.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.dailyexpense.db.Constants.DB_NAME
import com.example.dailyexpense.db.Constants.DB_VERSION
import com.example.dailyexpense.db.Constants.EXPENSE_DATE
import com.example.dailyexpense.db.Constants.EXPENSE_PRICE
import com.example.dailyexpense.db.Constants.EXPENSE_TYPE
import com.example.dailyexpense.db.Constants.ID
import com.example.dailyexpense.db.Constants.TABLE_NAME
import com.example.dailyexpense.model.Expense

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(sqlDb: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY,$EXPENSE_TYPE TEXT,$EXPENSE_PRICE INTEGER,$EXPENSE_DATE TEXT)"
        sqlDb?.execSQL(createTable)
    }

    override fun onUpgrade(sqlDb: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        sqlDb?.execSQL(dropTable)
        onCreate(sqlDb)
    }

    @SuppressLint("Range")
    fun getAllExpenses(): List<Expense>{
        val expenseList = ArrayList<Expense>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY id DESC"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    val expenses = Expense()
                    expenses.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    expenses.type = cursor.getString(cursor.getColumnIndex(EXPENSE_TYPE))
                    expenses.price = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXPENSE_PRICE)))
                    expenses.date = cursor.getString(cursor.getColumnIndex(EXPENSE_DATE))
                    expenseList.add(expenses)
                }while (cursor.moveToNext())
            }
        }
        cursor.close()
        return expenseList
    }

    fun insertExpense(expense: Expense): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(EXPENSE_TYPE, expense.type)
        values.put(EXPENSE_PRICE, expense.price)
        values.put(EXPENSE_DATE, expense.date)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    @SuppressLint("Range")
    fun searchExpense(_date: String): List<Expense>{
        val expenseList = ArrayList<Expense>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $EXPENSE_DATE LIKE ?"
        val cursor = db.rawQuery(selectQuery, arrayOf("%$_date%"))

        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    val expense = Expense()
                    expense.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    expense.type = cursor.getString(cursor.getColumnIndex(EXPENSE_TYPE))
                    expense.price = Integer.parseInt(cursor.getString(cursor.getColumnIndex(EXPENSE_PRICE)))
                    expense.date = cursor.getString(cursor.getColumnIndex(EXPENSE_DATE))
                    expenseList.add(expense)
                }while (cursor.moveToNext())
            }
        }

        cursor.close()

        return expenseList
    }

    fun deleteExpense(_id: Int): Boolean{
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, "$ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    fun updateExpense(expense: Expense): Boolean{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(EXPENSE_TYPE, expense.type)
        values.put(EXPENSE_PRICE, expense.price)
        values.put(EXPENSE_DATE, expense.date)
        val _success = db.update(TABLE_NAME, values, "$ID=?", arrayOf(expense.id.toString())).toLong()
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

}