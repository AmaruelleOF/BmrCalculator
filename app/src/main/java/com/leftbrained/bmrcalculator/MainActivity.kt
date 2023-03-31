package com.leftbrained.bmrcalculator

import android.os.Bundle
import android.text.SpannableStringBuilder
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.leftbrained.bmrcalculator.databinding.ActivityMainBinding
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun calculate(view: View) {
        val weight = findViewById<TextInputEditText>(R.id.weight)
        val height = findViewById<TextInputEditText>(R.id.height)
        val age = findViewById<TextInputEditText>(R.id.age)
        val gender = findViewById<RadioGroup>(R.id.radioGroup);
        val checked_gender = gender.checkedRadioButtonId;
        var bmr = 0.0;

        if (weight.text.toString() == "" || height.text.toString() == "" || age.text.toString() == "" || checked_gender.equals(null)) return

        try {
            if (checked_gender == R.id.maleRadio) bmr = 660.0 + (13.7 * weight.text.toString().toDouble()) +
                    (5 * height.text.toString().toDouble()) - (6.8 * age.text.toString().toDouble())
            else if (checked_gender == R.id.femaleRadio) bmr = 655.0 + (9.6 * weight.text.toString().toDouble()) +
                    (1.8 * height.text.toString().toDouble()) - (4.7 * age.text.toString().toDouble())
        } catch (e: Exception) {
            var exc = Toast.makeText(this, "ERROR: Incorrect format", Toast.LENGTH_LONG).show()
            return;
        }

        val activities = mapOf<Double, String>(
            round(bmr) to "Normal",
            round(bmr * 1.2) to "Passive lifestyle",
            round(bmr * 1.375) to "Low activity",
            round(bmr * 1.55) to "Medium activity",
            round(bmr * 1.725) to "High activity",
            round(bmr * 1.9) to "Extreme activity"
        )

        val activitiesKeys = activities.keys.toDoubleArray()

        MaterialAlertDialogBuilder(this)
            .setTitle("Your calories measurement")
            .setMessage(String.format("${activitiesKeys[0]}: Normal value (w/o activity ratio)\n" +
                "${activitiesKeys[1]}: ${activities[round(bmr * 1.2)]}\n" +
                    "${activitiesKeys[2]}: ${activities[round(bmr * 1.375)]}\n" +
                        "${activitiesKeys[3]}: ${activities[round(bmr * 1.55)]}\n" +
                        "${activitiesKeys[4]}: ${activities[round(bmr * 1.725)]}\n" +
                        "${activitiesKeys[5]}: ${activities[round(bmr * 1.9)]}")).setPositiveButton("OK") { dialog, which -> }.show()
    }

    fun activitiesInfo(view: View) {
        MaterialAlertDialogBuilder(this).setTitle("Activity Info").setMessage(
            "Passive: This level of activity is characterized by a sedentary lifestyle with little or no physical activity. Examples of this level of activity include sitting at a desk all day, watching TV, or playing video games.\n\n" +
                    "Low: This level of activity is characterized by light physical activity, such as walking or doing household chores. Examples of this level of activity include taking a leisurely walk, gardening, or cooking.\n\n" +
                    "Medium: This level of activity is characterized by moderate physical activity, such as jogging, cycling, or swimming. Examples of this level of activity include going for a run, playing a sport, or doing a workout at the gym.\n\n" +
                    "High: This level of activity is characterized by intense physical activity, such as competitive sports or heavy lifting. Examples of this level of activity include playing a contact sport, weightlifting, or doing a high-intensity workout.\n\n" +
                    "Extreme: This level of activity is characterized by extreme physical exertion, such as marathon running or mountaineering. Examples of this level of activity include running a marathon, climbing a mountain, or doing an extreme endurance event."
        ).setPositiveButton("OK") { dialog, which -> }.show()
    }
}