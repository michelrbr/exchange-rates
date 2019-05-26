package br.com.mxel.exchangerates.domain

import io.reactivex.Scheduler

class SchedulerProvider(
    val mainThread: Scheduler,
    val backgroundThread: Scheduler
)