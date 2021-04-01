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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_list_students.view.*

class AdapterStudents(
    var students: MutableList<Student>,
    val studentsBackup: MutableList<Student>,
    val onItemClick: CallbackAdapterStudents
) :
    RecyclerView.Adapter<AdapterStudents.ViewHolder>(), Filterable {

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


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_name_student.text = students[position].name
        holder.tv_number_phone.text = (students[position].numberPhone).toString()
        holder.tv_education.text = students[position].education

        holder.itemView.setOnClickListener {
            onItemClick.onItemClick(position)
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
                for (student in studentsBackup) {
                    if (student.name.toLowerCase().trim().contains(commonPattern)) {
                        studentsFiltered.add(student)
                    } else if (student.numberPhone.toString().toLowerCase().trim()
                            .contains(commonPattern)
                    ) {
                        studentsFiltered.add(student)
                    } else if (student.yearOfBirth.toString().toLowerCase().trim()
                            .contains(commonPattern)
                    ) {
                        studentsFiltered.add(student)
                    } else if (student.education.toLowerCase().trim().contains(commonPattern)) {
                        studentsFiltered.add(student)
                    } else if (student.nameSchool.contains(commonPattern)) {
                        studentsFiltered.add(student)
                    } else if (student.major.contains(commonPattern)) {
                        studentsFiltered.add(student)
                    }
                }
                result.values = studentsFiltered
            }

            return result
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            students.clear()
            var studentsFiltered = results?.values as MutableList<Student>
            students.addAll(studentsFiltered)
            notifyDataSetChanged()
        }

    }
}