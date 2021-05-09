package com.example.pucquiz.callbacks

import com.example.pucquiz.callbacks.models.GenericCallback

interface OperationCallback {
    fun callbackResponse(operation: GenericCallback)
}