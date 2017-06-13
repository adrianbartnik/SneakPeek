package de.sneakpeek.ui.main.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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

        val dataSet = LineDataSet(entries, getString(R.string.statistic_fragment_distribution_description));
        dataSet.valueFormatter = IValueFormatter { value, _, _, _ -> String.format("%.1f%%", value * 100) }
        dataSet.color = ContextCompat.getColor(context, R.color.accent)

        var sum = 0
        val accumulated = IntArray(entries.size)
        for (i in 0..entries.lastIndex) {
            sum += stats[i]
            accumulated[i] = sum
        }

        val cumulativeDistribution = accumulated.map { it.toFloat() / sampleSize }.mapIndexed { index, i -> Entry(index.toFloat(), i) }

        val dataSetCumulative = LineDataSet(cumulativeDistribution, getString(R.string.statistic_fragment_cumulative_distribution_description))
        dataSetCumulative.valueFormatter = IValueFormatter { value, _, _, _ -> String.format("%.1f%%", value * 100) }
        dataSetCumulative.color = ContextCompat.getColor(context, R.color.primary)
        dataSetCumulative.setCircleColor(ContextCompat.getColor(context, R.color.primary))

        lineChart?.let {
            it.xAxis?.position = XAxis.XAxisPosition.BOTTOM
            it.xAxis?.isGranularityEnabled = true
            it.xAxis?.granularity = 1f
            it.xAxis?.mAxisMaximum = stats.size.toFloat()
            it.axisRight?.isEnabled = false
            it.xAxis?.labelCount = 10

            it.legend?.isEnabled = true
            it.legend?.isWordWrapEnabled = true
            it.legend?.textSize = 14f

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(dataSet)
            dataSets.add(dataSetCumulative)

            it.data = LineData(dataSets)
            it.description?.isEnabled = true
            it.description?.text = "Dataset size: $sampleSize"
            it.description?.textSize = 14f

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

            var indexOfCorrectPrediction = prediction.movies.find { it.title == movie.title }?.position ?: -1

            indexOfCorrectPrediction -= 1

            if (indexOfCorrectPrediction != -2) {
                correctPrediction[indexOfCorrectPrediction] += 1
            }
        }

        return Pair(correctPrediction, predictions.size)
    }

    companion object {

        fun newInstance(): StatisticsFragment {
            return StatisticsFragment()
        }
    }
}