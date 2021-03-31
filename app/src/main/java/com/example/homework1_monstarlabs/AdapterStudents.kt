package com.example.homework1_monstarlabs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.homework1_monstarlabs.MainActivity.Companion.ALL
import com.example.homework1_monstarlabs.MainActivity.Companion.SORT_BY_NAME
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_list_students.view.*
import kotlinx.android.synthetic.main.layout_dialog.view.*

class AdapterStudents(
    val context: Context,
    var students: MutableList<Student>,
    val studentsBackup: MutableList<Student>
) :
    RecyclerView.Adapter<AdapterStudents.ViewHolder>(), Filterable {

    lateinit var education:String

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_name_student = view.tv_name_student
        var tv_number_phone = view.tv_number_phone
        var tv_education = view.tv_education
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_students, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return students.size
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_name_student.text = students[position].name
        holder.tv_number_phone.text = (students[position].numberPhone).toString()
        holder.tv_education.text = students[position].education

        holder.itemView.setOnClickListener {

            var dialogBuilder = AlertDialog.Builder(context)
            var view = LayoutInflater.from(context).inflate(R.layout.layout_dialog,null,false)
            dialogBuilder.setView(view)
            dialogBuilder.setNegativeButton("Delete",DialogInterface.OnClickListener { dialog, which ->
                students.removeAt(position)
                studentsBackup.removeAt(position)
                notifyDataSetChanged()
            })
            dialogBuilder.setPositiveButton("Update",DialogInterface.OnClickListener { dialog, which ->

                var name = view.edt_name_student_dialog.text.toString()
                var yearOfBirth = (view.edt_year_of_birth_dialog.text).toString()
                var numberPhone = (view.edt_sdt_dialog.text).toString()
                var nameSchool = view.edt_name_school_dialog.text.toString()
                var major = view.edt_major_dialog.text.toString()

                when {
                    name.equals("") -> {
                        view.edt_name_student_dialog.error = "Require enter name"
                    }
                    yearOfBirth.equals("") -> {
                        view.edt_year_of_birth_dialog.error = "Require enter year of birth"
                    }
                    numberPhone.equals("") -> {
                        view.edt_sdt_dialog.error = "Require enter number phone"
                    }
                    nameSchool.equals("") -> {
                        view.edt_name_school_dialog.error = "Require enter school's name"
                    }
                    major.equals("") -> {
                        view.edt_major_dialog.error = "Require enter major"
                    }
                    else -> {
                        var student = Student(
                            name,
                            yearOfBirth.toInt(),
                            numberPhone.toInt(),
                            education,
                            nameSchool,
                            major
                        )
                        students.removeAt(position)
                        studentsBackup.removeAt(position)
                        studentsBackup.add(position,student)
                        students.add(position,student)
                        notifyDataSetChanged()
                    }
          }
            })
            dialogBuilder.setNeutralButton("Cancel",DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            dialogBuilder.create()
            view.edt_name_student_dialog.setText(students[position].name)
            view.edt_year_of_birth_dialog.setText(students[position].yearOfBirth.toString())
            view.edt_sdt_dialog.setText(students[position].numberPhone.toString())
            view.edt_name_school_dialog.setText(students[position].nameSchool)
            view.edt_major_dialog.setText(students[position].major)
            var adapterSpiner = ArrayAdapter.createFromResource(
                context,
                R.array.education,
                android.R.layout.simple_spinner_item
            )
            adapterSpiner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            view.sp_education_dialog.adapter = adapterSpiner
            view.sp_education_dialog.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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
            dialogBuilder.show()

        }
    }

    override fun getFilter(): Filter {
        return filterStudent
    }

    var filterStudent = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var result = FilterResults()
            var studentsFiltered: MutableList<Student> = mutableListOf()
            var pattern = constraint.toString()

            if (pattern == MainActivity.COLLEGE || pattern == MainActivity.UNIVERSITY) {
                students.clear()
                students.addAll(studentsBackup)
                for (student in students) {
                    if (student.education == (pattern)) {
                        studentsFiltered.add(student)
                    }
                }
                result.values = studentsFiltered
            } else if (pattern == ALL) {
                result.values = studentsBackup
            } else {
                var commonPattern = pattern.toLowerCase().trim()
                for (student in students) {
                    var informationStudent: String =
                        "${student.name} - ${student.numberPhone} - ${student.yearOfBirth} - ${student.education} - ${student.nameSchool} -${student.major}"

                    if (informationStudent.contains(commonPattern)) {
                        studentsFiltered.add(student)
                    }
                }
                result.values = studentsFiltered
            }

            return result
        }

        public

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            students.clear()
            var studentsFiltered = results?.values as MutableList<Student>
            students.addAll(studentsFiltered)
            notifyDataSetChanged()
        }

    }
}