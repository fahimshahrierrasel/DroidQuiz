package com.treebricks.quiz.model
import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by fahim on 6/21/17.
 */

open class QuizQA(
        open var question: String? = null,
        open var options: RealmList<QuizOptions>? = null,
        open var answer: String? = null,
        open var explanation: String? = null,
        open var image: String? = null
): RealmObject() {}