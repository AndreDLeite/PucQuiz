package com.example.pucquiz.repositories.firebasertdb

import android.util.Log
import com.example.pucquiz.callbacks.FirebaseGradeQuestionsCallBack
import com.example.pucquiz.callbacks.FirebaseTeacherQuestionsCallback
import com.example.pucquiz.callbacks.FirebaseUserAddInfoCallback
import com.example.pucquiz.callbacks.FirebaseUserAddInfoUpdateCallback
import com.example.pucquiz.callbacks.FirebaseUserCallback
import com.example.pucquiz.callbacks.FirebaseUserMedalsCallback
import com.example.pucquiz.callbacks.OperationCallback
import com.example.pucquiz.callbacks.QuestionsAdditionalInfoCallback
import com.example.pucquiz.callbacks.SingleQuestionAddInfoCallback
import com.example.pucquiz.callbacks.models.GenericCallback
import com.example.pucquiz.callbacks.models.UserAdditionalInfoResponse
import com.example.pucquiz.callbacks.models.UsersRankingResponse
import com.example.pucquiz.models.GradeEnum
import com.example.pucquiz.models.GradesAnswers
import com.example.pucquiz.models.Question
import com.example.pucquiz.models.QuestionAdditionalInfo
import com.example.pucquiz.models.User
import com.example.pucquiz.models.UserAdditionalInfo
import com.example.pucquiz.models.UserMedals
import com.example.pucquiz.shared.AppConstants
import com.example.pucquiz.shared.AppConstants.FIREBASE_QUESTIONS_ADDITIONAL_INFO_BUCKET
import com.example.pucquiz.shared.AppConstants.FIREBASE_TEACHER_QUESTIONS_BUCKET
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_BUCKET
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_INFO_BUCKET
import com.example.pucquiz.shared.AppConstants.FIREBASE_USER_MEDALS_BUCKET
import com.example.pucquiz.ui.shared.enums.QuizType
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseRTDBRepository : IFirebaseRTDBRepository {
    private val firebaseRTDBInstance = FirebaseDatabase.getInstance()

    //region UserAdditionalInfo
    override suspend fun fetchAllUsersAdditionalInfo(callback: FirebaseUserAddInfoCallback) {
        firebaseRTDBInstance.getReference(FIREBASE_USER_INFO_BUCKET).get()
            .addOnCompleteListener { task ->
                val response = UsersRankingResponse()
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.usersAdditionalInfoList = result.children.map { snapShot ->
                            snapShot.getValue(UserAdditionalInfo::class.java)!!
                        }
                    }
                } else {
                    response.exception = task.exception
                }
                callback.onResponse(response)
            }
    }

    override suspend fun fetchUserInfoByUserId(userId: String, callback: FirebaseUserCallback) {
        firebaseRTDBInstance.getReference(FIREBASE_USER_BUCKET)
            .child(userId).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userProfile = snapshot.getValue(User::class.java)
                        Log.d("user", "$userProfile")
                        userProfile?.let {
                            callback.onResponse(userProfile)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback.onResponse(null)
                    }

                }
            )
    }

    override suspend fun fetchUserAdditionalInfoByUserId(
        userId: String,
        callback: FirebaseUserAddInfoCallback
    ) {
        firebaseRTDBInstance.getReference(FIREBASE_USER_INFO_BUCKET).child(userId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userAdditionalInfo = snapshot.getValue(UserAdditionalInfo::class.java)
                        Log.d("user addinfo", "$userAdditionalInfo")
                        userAdditionalInfo?.let {
                            callback.onResponse(UsersRankingResponse(listOf(userAdditionalInfo)))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback.onResponse(UsersRankingResponse(listOf()))
                    }

                }
            )
    }
    //endregion

    //region UserMedals
    override suspend fun fetchUserMedalsByUserId(
        userId: String,
        callback: FirebaseUserMedalsCallback
    ) {
        firebaseRTDBInstance.getReference(FIREBASE_USER_MEDALS_BUCKET)
            .child(userId).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userMedals = snapshot.getValue(UserMedals::class.java)
                        Log.d("user medals", "$userMedals")
                        callback.onResponse(userMedals)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback.onResponse(null)
                    }

                }
            )
    }
    //endregion

    //region TeacherQuestions

    override suspend fun fetchTeacherQuestions(
        userId: String,
        callback: FirebaseTeacherQuestionsCallback
    ) {
        val firebaseRTDBInstance =
            firebaseRTDBInstance.getReference(FIREBASE_TEACHER_QUESTIONS_BUCKET)
        val query = firebaseRTDBInstance
            .orderByChild("teacherId")
            .equalTo(userId)

        query.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val response = mutableListOf<Question>()
                    val questionList = snapshot.children.map { snapShot ->
                        snapShot.getValue(Question::class.java)!!
                    }
                    Log.e("firebase response", questionList.toString())
                    response.addAll(questionList)
                    callback.onResponse(response)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.onResponse(emptyList())
                }
            }
        )
    }

    //endregion

    //region questions

    override suspend fun fetchGradeQuestionsByQuestionType(
        grade: GradeEnum,
        questionType: QuizType,
        callback: FirebaseGradeQuestionsCallBack
    ) {
        val firebaseRTDBInstance =
            firebaseRTDBInstance.getReference(FIREBASE_TEACHER_QUESTIONS_BUCKET)
        val query = firebaseRTDBInstance
            .orderByChild("questionGrade")
            .equalTo(grade.toString())

        query.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val response = mutableListOf<Question>()
                    val questionList = snapshot.children.map { snapShot ->
                        snapShot.getValue(Question::class.java)!!
                    }.filter { it.questionType == questionType }
                    Log.e("firebase response", questionList.toString())
                    response.addAll(questionList)
                    callback.onResponse(response)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.onResponse(emptyList())
                }
            }
        )
    }

    //endregion

    override suspend fun updateUserQuestionsAnswered(
        userId: String,
        newUserAdditionalInfo: UserAdditionalInfo,
        callback: FirebaseUserAddInfoUpdateCallback
    ) {
        firebaseRTDBInstance.getReference(FIREBASE_USER_INFO_BUCKET)
            .child(userId)
            .setValue(newUserAdditionalInfo)
            .addOnCompleteListener { itRTDBTask ->
                if (itRTDBTask.isSuccessful) {
                    callback.onResponse(UserAdditionalInfoResponse("Success", true))
                    Log.e("PQP", "Funcionou a bagaça de questoes!! :O")
                } else {
                    callback.onResponse(
                        UserAdditionalInfoResponse(
                            "Erro de comunicação com o servidor. Por favor tente novamente mais tarde.",
                            false
                        )
                    )
                }
            }
    }

    //region Medals

    override suspend fun updateUserMedals(
        userId: String,
        newUserMedals: UserMedals,
        callback: OperationCallback
    ) {
        firebaseRTDBInstance.getReference(FIREBASE_USER_MEDALS_BUCKET)
            .child(userId)
            .setValue(newUserMedals)
            .addOnCompleteListener { itRTDBTask ->
                if (itRTDBTask.isSuccessful) {
                    callback.callbackResponse(GenericCallback("Success", true))
                    Log.e("PQP", "Funcionou a bagaça de medalhas!! :O")
                } else {
                    callback.callbackResponse(
                        GenericCallback(
                            "Erro de comunicação com o servidor. Por favor tente novamente mais tarde.",
                            false
                        )
                    )
                }
            }
    }
    //endregion


    //region QuestionsAdditionalInfo

    override suspend fun sendQuestionAddInfoToFirebase(
        questionId: String,
        questionAdditionalInfo: QuestionAdditionalInfo
    ): Boolean {
        var operationResult = true
        FirebaseDatabase.getInstance()
            .getReference(AppConstants.FIREBASE_QUESTIONS_ADDITIONAL_INFO_BUCKET)
            .child(questionId)
            .setValue(questionAdditionalInfo)
            .addOnCompleteListener { itRTDBTask ->
                operationResult = when {
                    itRTDBTask.isSuccessful -> {
                        operationResult = true
                        Log.e("QAI Creation", "Created with success!")
                        true
                    }

                    else -> {
                        operationResult = false
                        Log.e("QAI Creation", "Failed to create...")
                        false
                    }
                }
            }
        return operationResult
    }


    override suspend fun fetchQuestionAdditionalInfo(callback: QuestionsAdditionalInfoCallback) {
        firebaseRTDBInstance.getReference(FIREBASE_QUESTIONS_ADDITIONAL_INFO_BUCKET).get()
            .addOnCompleteListener { task ->
                var response = listOf<QuestionAdditionalInfo>()
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response = result.children.map { snapShot ->
                            snapShot.getValue(QuestionAdditionalInfo::class.java)!!
                        }
                    }
                }
                Log.e("QAI Fetched", response.toString())
                callback.onResponse(response)
            }
    }

    override suspend fun fetchQuestionAdditionalInfoByQuestionId(
        questionId: String,
        callback: SingleQuestionAddInfoCallback
    ) {
        firebaseRTDBInstance.getReference(FIREBASE_QUESTIONS_ADDITIONAL_INFO_BUCKET).child(questionId)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val questionAdditionalInfo = snapshot.getValue(QuestionAdditionalInfo::class.java)
                        Log.d("QAI fetch", "$questionAdditionalInfo")
                        questionAdditionalInfo?.let {
                            callback.onResponse(data = questionAdditionalInfo)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback.onResponse(null)
                    }

                }
            )
    }

    //endRegion
}