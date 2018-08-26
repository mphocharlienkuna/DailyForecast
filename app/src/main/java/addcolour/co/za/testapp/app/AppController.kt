package addcolour.co.za.testapp.app

import android.app.Application
import android.content.Context

import addcolour.co.za.testapp.network.ApiFactory
import addcolour.co.za.testapp.network.ApiInterface
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class AppController : Application() {

    var apiInterface: ApiInterface? = null
        get() {
            if (field == null) {
                this.apiInterface = ApiFactory.create()
            }

            return field
        }
    private var scheduler: Scheduler? = null

    fun subscribeScheduler(): Scheduler {
        if (scheduler == null) {
            scheduler = Schedulers.io()
        }

        return scheduler as Scheduler
    }

    fun setScheduler(scheduler: Scheduler) {
        this.scheduler = scheduler
    }

    companion object {

        private operator fun get(context: Context): AppController {
            return context.applicationContext as AppController
        }

        fun create(context: Context): AppController {
            return AppController[context]
        }
    }
}