package com.treebricks.quiz

import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.treebricks.quiz.model.QuizQA
import io.realm.Realm
import kotlin.properties.Delegates
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import android.view.WindowManager
import android.os.Build
import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Outline
import android.media.MediaPlayer
import android.view.ViewOutlineProvider

class MainActivity : AppCompatActivity(), View.OnClickListener{

    val quizes = ArrayList<QuizQA>()
    var currentQuestion = 0
    var questionView: TextView by Delegates.notNull()
    var scoreView: TextView by Delegates.notNull()
    var option1: Button by Delegates.notNull()
    var option2: Button by Delegates.notNull()
    var option3: Button by Delegates.notNull()
    var option4: Button by Delegates.notNull()
    var nextButton: ImageButton by Delegates.notNull()
    var infoButton: ImageButton by Delegates.notNull()
    var explainButton: Button by Delegates.notNull()
    var container: RelativeLayout by Delegates.notNull()
    var buzzer:MediaPlayer by Delegates.notNull()
    var answerClicked = false
    var tried = 0
    var correct = 0


    private var realm: Realm by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        copyBundledRealmFile(resources.assets.open("default.realm"), "default.realm")
        realm = Realm.getDefaultInstance()

        container = findViewById(R.id.container) as RelativeLayout
        scoreView = findViewById(R.id.scoreView) as TextView
        questionView = findViewById(R.id.questionView) as TextView
        explainButton = findViewById(R.id.explainButton) as Button
        option1 = findViewById(R.id.option1) as Button
        option2 = findViewById(R.id.option2) as Button
        option3 = findViewById(R.id.option3) as Button
        option4 = findViewById(R.id.option4) as Button
        nextButton = findViewById(R.id.nextFabButton) as ImageButton
        infoButton = findViewById(R.id.infoButton) as ImageButton
        option1.setOnClickListener(this)
        option2.setOnClickListener(this)
        option3.setOnClickListener(this)
        option4.setOnClickListener(this)
        nextButton.setOnClickListener(this)
        explainButton.setOnClickListener(this)
        infoButton.setOnClickListener(this)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#A63F51B5")

        nextButton.outlineProvider = object : ViewOutlineProvider() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun getOutline(view: View, outline: Outline) {
                val diameter = resources.getDimensionPixelSize(R.dimen.diameter)
                outline.setOval(0, 0, diameter, diameter)
            }
        }
        nextButton.clipToOutline = true



        val query = realm.where(QuizQA::class.java)
        val result = query.findAll()
        for (quizqa: QuizQA in result){
            quizes.add(quizqa)
        }

        questionView.text = quizes[currentQuestion].question
        option1.text = quizes[currentQuestion].options?.get(0)?.option
        option2.text = quizes[currentQuestion].options?.get(1)?.option
        option3.text = quizes[currentQuestion].options?.get(2)?.option
        option4.text = quizes[currentQuestion].options?.get(3)?.option

        updateScore()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            R.id.option1 -> {
                if (quizes[currentQuestion].answer == option1.text){
                    rightAnswer(option1)
                }else {
                    wrongAnswer(option1)
                }
            }

            R.id.option2 -> {
                if (quizes[currentQuestion].answer == option2.text){
                    rightAnswer(option2)
                }else{
                    wrongAnswer(option2)
                }
            }

            R.id.option3 -> {
                if (quizes[currentQuestion].answer == option3.text){
                    rightAnswer(option3)
                }else{
                    wrongAnswer(option3)
                }
            }

            R.id.option4 -> {
                if (quizes[currentQuestion].answer == option4.text){
                    rightAnswer(option4)
                }else{
                    wrongAnswer(option4)
                }
            }

            R.id.nextFabButton ->{
                currentQuestion++
                if (currentQuestion == quizes.size) {
                    currentQuestion = 0
                    tried = 0
                    correct = 0
                }

                questionView.text = quizes[currentQuestion].question
                option1.text = quizes[currentQuestion].options?.get(0)?.option
                option2.text = quizes[currentQuestion].options?.get(1)?.option
                option3.text = quizes[currentQuestion].options?.get(2)?.option
                option4.text = quizes[currentQuestion].options?.get(3)?.option

                option1.background.clearColorFilter()
                option2.background.clearColorFilter()
                option3.background.clearColorFilter()
                option4.background.clearColorFilter()

                container.setBackgroundColor(Color.parseColor("#FAFAFA"))
                window.statusBarColor = Color.parseColor("#A63F51B5")

                answerClicked = false
                explainButton.visibility = View.GONE

                updateScore()

            }

            R.id.explainButton -> {
                showExplanation()
            }

            R.id.infoButton -> {
                startActivity(Intent(this, AboutActivity::class.java))
            }

            else -> {
                Toast.makeText(this, "Else", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun rightAnswer(button: Button){
        if (!answerClicked) {

            correct++
            tried++
            window.statusBarColor = Color.parseColor("#B9F6CA")
            button.background.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY)
            container.setBackgroundColor(Color.parseColor("#B9F6CA"))
            answerClicked = true
            explainButton.visibility = View.VISIBLE

            buzzer = MediaPlayer.create(this, R.raw.correct)
            buzzer.setOnCompletionListener({
                buzzer.reset()
                buzzer.release()
            })
            buzzer.start()
            updateScore()
        }
    }

    private fun wrongAnswer(button: Button){
        if (!answerClicked) {

            tried++
            window.statusBarColor = Color.parseColor("#FF8A80")
            button.background.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY)
            container.setBackgroundColor(Color.parseColor("#FF8A80"))
            answerClicked = true

            if(option1 != button && option1.text == quizes[currentQuestion].answer){
                option1.background.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY)
            }else if (option2 != button && option2.text == quizes[currentQuestion].answer){
                option2.background.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY)
            }else if (option3 != button && option3.text == quizes[currentQuestion].answer){
                option3.background.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY)
            }else if (option4 != button && option4.text == quizes[currentQuestion].answer){
                option4.background.setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY)
            }
            explainButton.visibility = View.VISIBLE
            buzzer = MediaPlayer.create(this, R.raw.wrong)
            buzzer.setOnCompletionListener({
                buzzer.reset()
                buzzer.release()
            })
            buzzer.start()
            updateScore()
        }
    }

    private fun showExplanation(){
        val modalBottomSheet = ExplainBottomSheet().newInstance(quizes[currentQuestion].answer as String,
                quizes[currentQuestion].explanation as String, this.resources.getIdentifier(quizes[currentQuestion].image,
                "drawable", this.packageName))
        modalBottomSheet.show(supportFragmentManager, "bottom sheet")
    }

    private fun copyBundledRealmFile(inputStream: InputStream, outFileName: String): String? {
        try {
            val file = File(this.filesDir, outFileName)
            val outputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var bytesRead: Int  = inputStream.read(buf)
            while ( bytesRead> 0) {
                outputStream.write(buf, 0, bytesRead)
                bytesRead  = inputStream.read(buf)
            }
            outputStream.close()
            return file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    private fun updateScore(){
        val scoreText = "$correct correct out of $tried try"
        scoreView.text = scoreText
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


}
