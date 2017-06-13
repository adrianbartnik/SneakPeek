package de.sneakpeek.ui.main.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IValueFormatter
import de.sneakpeek.R
import de.sneakpeek.data.SneakPeekDatabaseHelper
import de.sneakpeek.util.inflate


class StatisticsFragment : Fragment() {

    var lineChart: LineChart? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val layout = container?.inflate(R.layout.fragment_statistics) as LinearLayout

        lineChart = layout.findViewById(R.id.fragment_statistics_chart) as LineChart

        setupChart()

        return layout
    }

    private fun setupChart() {
        val (stats, sampleSize) = calcStatistic()

        if (sampleSize == 0) {
            return
        }

        val entries = stats.map { it.toFloat() / sampleSize }.mapIndexed { index, i -> Entry(index.toFloat(), i) }

        val dataSet = LineDataSet(entries, "Probability of n-th prediction is correct");
        dataSet.valueFormatter = IValueFormatter { value, _, _, _ -> String.format("%.1f%%", value * 100) }
        dataSet.color = ContextCompat.getColor(context, R.color.accent)

        lineChart?.let {
            Log.d("Stats", "Size: " + entries.size + " " + stats + " " + stats.size)

            it.xAxis?.position = XAxis.XAxisPosition.BOTTOM
            it.xAxis?.isGranularityEnabled = true
            it.xAxis?.granularity = 1f
            it.xAxis?.mAxisMaximum = stats.size.toFloat()
            it.axisRight?.isEnabled = false
            it.xAxis?.labelCount = 10

            it.legend?.isEnabled = true

            val lineData = LineData(dataSet)

            it.data = lineData
            it.description?.isEnabled = true
            it.invalidate()
        }
    }

    /**
     * Returns an array with the number of correct predictions and the number of sneaks
     */
    private fun calcStatistic(): Pair<IntArray, Int> {
        val predictions = SneakPeekDatabaseHelper.GetInstance(context).getMoviePredictions()
        val actualMovies = SneakPeekDatabaseHelper.GetInstance(context).getActualMovies().reversed()

        if (actualMovies.isEmpty()) {
            return Pair(kotlin.IntArray(0), 0)
        }

        val correctPrediction = IntArray(predictions.map { it.movies.size }.max() ?: 15)

        for (i in 0..predictions.lastIndex) {
            val prediction = predictions[i]
            val movie = actualMovies[i]

            Log.d("Stats", "Sizes: " + actualMovies.size + " " + prediction.movies.size + " " + prediction.movies)

            var indexOfCorrectPrediction = prediction.movies.find { it.title == movie.title }?.position ?: -1

            indexOfCorrectPrediction -= 1

            if (indexOfCorrectPrediction != -2) {
                Log.d("Stats", "Correct prediction: $indexOfCorrectPrediction $movie")
                correctPrediction[indexOfCorrectPrediction] += 1
            } else {
                Log.d("Stats", "Not identified: $indexOfCorrectPrediction $movie $prediction")
            }
        }

        return Pair(correctPrediction, predictions.size)
    }

    companion object {

        private val TAG = StatisticsFragment::class.java.simpleName

        fun newInstance(): StatisticsFragment {
            return StatisticsFragment()
        }
    }
}