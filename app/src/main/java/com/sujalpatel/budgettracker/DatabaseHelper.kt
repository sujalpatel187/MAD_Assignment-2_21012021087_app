package com.sujalpatel.budgettracker

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ExpenseTrackerDB"
        private const val DATABASE_VERSION = 1
        const val TABLE_TRANSACTIONS = "transactions"
        const val COLUMN_ID = "_id"
        const val COLUMN_LABEL = "label"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_TRANSACTIONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_LABEL TEXT,
                $COLUMN_AMOUNT REAL,
                $COLUMN_DESCRIPTION TEXT
            );
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        onCreate(db)
    }

    fun insertTransaction(transaction: Transaction) {
        val values = ContentValues()
        values.put(COLUMN_LABEL, transaction.label)
        values.put(COLUMN_AMOUNT, transaction.amount)
        values.put(COLUMN_DESCRIPTION, transaction.description)

        val db = writableDatabase
        db.insert(TABLE_TRANSACTIONS, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllTransactions(): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val query = "SELECT * FROM $TABLE_TRANSACTIONS"

        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val label = cursor.getString(cursor.getColumnIndex(COLUMN_LABEL))
                val amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT))
                val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))

                val transaction = Transaction(id, label, amount, description)
                transactions.add(transaction)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return transactions
    }
}