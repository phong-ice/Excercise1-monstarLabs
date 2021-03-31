package com.example.homework1_monstarlabs

import android.app.Activity
import android.content.Context
import android.media.MediaCodec
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(){

    lateinit var students: MutableList<Student>
    lateinit var studentsBackup: MutableList<Student>
    lateinit var adapterStudents: AdapterStudents
    lateinit var education:String

    companion object {
        const val UNIVERSITY = "University"
        const val COLLEGE = "College"
        const val ALL = "All"
        const val SORT_BY_NAME = "sortByName"
        const val SORT_BY_NUMBER_PHONE = "sortByNumberPhone"
        const val SORT_BY_YEAR_OF_BIRTH = "sortByYearOfBirth"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        students = mutableListOf()
        studentsBackup = mutableListOf()
        adapterStudents = AdapterStudents(this, students, studentsBackup)
        lv_students.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterStudents
        }

        var adapterSpiner = ArrayAdapter.createFromResource(
            this,
            R.array.education,
            android.R.layout.simple_spinner_item
        )
        adapterSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_education.adapter = adapterSpiner
        sp_education.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                education = parent!!.getItemAtPosition(position) as String
            }

        }
    }

    fun addStudent(view: View) {

        var name = edt_name_student.text.toString()
        var yearOfBirth = (edt_year_of_birth.text).toString()
        var numberPhone = (edt_sdt.text).toString()
        var nameSchool = edt_name_school.text.toString()
        var major = edt_major.text.toString()


        when {
            name.equals("") -> {
                edt_name_student.setError("Require enter name")
            }
            yearOfBirth.equals("") -> {
                edt_year_of_birth.setError("Require enter year of birth")
            }
            numberPhone.equals("") -> {
                edt_sdt.setError("Require enter number phone")
            }
            nameSchool.equals("") -> {
                edt_name_school.setError("Require enter school's name")
            }
            major.equals("") -> {
                edt_major.setError("Require enter major")
            }

//            !edt_year_of_birth.validateYearOfBirth() -> {
//                edt_year_of_birth.error =
//                    "Year of birth must has 4 character and it must be less then year now "
//            }
            else -> {
                if (students.size == 0) {
                    var student = Student(
                        name,
                        yearOfBirth.toInt(),
                        numberPhone.toInt(),
                        education,
                        nameSchool,
                        major
                    )
                    students.add(0, student)
                    studentsBackup.add(0,student)
                    adapterStudents.notifyDataSetChanged()

                    edt_name_student.setText("")
                    edt_year_of_birth.setText("")
                    edt_sdt.setText("")
                    edt_name_school.setText("")
                    edt_major.setText("")
                    this.hideKeyboard(view)
                } else {
                    var isNumberPhoneAvailable: Boolean = false
                    for (student in students) {
                        if (numberPhone == student.numberPhone.toString()) {
                            edt_sdt.error = "This number phone available"
                            isNumberPhoneAvailable = true
                            break
                        }
                    }
                    if (!isNumberPhoneAvailable) {
                        var student = Student(
                            name,
                            yearOfBirth.toInt(),
                            numberPhone.toInt(),
                            education,
                            nameSchool,
                            major
                        )
                        students.add(0, student)
                        studentsBackup.add(0, student)
                        adapterStudents.notifyDataSetChanged()

                        edt_name_student.setText("")
                        edt_year_of_birth.setText("")
                        edt_sdt.setText("")
                        edt_name_school.setText("")
                        edt_major.setText("")
                        this.hideKeyboard(view)
                    }
                }

            }
        }
    }

    public fun updateStudens(){
        Toast.makeText(this, "hello my name is Phong", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_student, menu)

        var menuItem = menu?.findItem(R.id.item_search)
        var searchView: SearchView = menuItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterStudents.filterStudent.filter(newText.toString())
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_college -> {
                adapterStudents.filterStudent.filter(COLLEGE)
            }
            R.id.item_university -> {
                adapterStudents.filterStudent.filter(UNIVERSITY)
            }
            R.id.item_show_all -> {
                adapterStudents.filterStudent.filter(ALL)
            }
            R.id.item_sortByName -> {
                students.sortBy { it.name }
                adapterStudents.notifyDataSetChanged()
            }
            R.id.item_sortByNumberPhone -> {
                students.sortBy { it.numberPhone }
                adapterStudents.notifyDataSetChanged()
            }
            R.id.item_sortByYearOfBirth -> {
                students.sortBy { it.yearOfBirth }
                adapterStudents.notifyDataSetChanged()
            }
        }
        return true
    }

    //    fun EditText.validateYearOfBirth(): Boolean {
//        var year = this.text.toString().toInt()
//        var data = Date()
//        var simpleFormatDate = SimpleDateFormat("yyyy")
//        var yearNovv = simpleFormatDate.format(data)
//        var yearNow: Int = yearNovv.toInt()
//        return text.length > 4 && year > yearNow
//    }
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

