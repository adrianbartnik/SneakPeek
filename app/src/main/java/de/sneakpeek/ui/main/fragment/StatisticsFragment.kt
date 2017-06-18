package de.sneakpeek.ui.main.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import kotlinx.android.synthetic.main.fragment_statistics.*


class StatisticsFragment : Fragment() {

    private val actual_movies: TextView by lazy { fragment_statistics_actual_movies }
    private val studios_number: TextView by lazy { fragment_statistics_studios_number }
    private val total_predictions: TextView by lazy { fragment_statistics_total_predictions }
    private val unique_predictions: TextView by lazy { fragment_statistics_unique_predictions }
    private val statistics_chart: LineChart by lazy { fragment_statistics_chart }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_statistics)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChart()
    }

    fun setupChart() {
        val (stats, statistics) = calcStatistic()

        if (statistics.numberOfMovies == -1) {
            return
        }

        actual_movies.text = "${statistics.numberOfMovies}"
        studios_number.text = "${statistics.numberOfStudios}"
        total_predictions.text = "${statistics.numberOfPredictions}"
        unique_predictions.text = "${statistics.numberOfUniquePredictions}"

        val entries = stats.map { it.toFloat() / statistics.numberOfMovies }.mapIndexed { index, i -> Entry(index.toFloat() + 1, i) }

        val dataSet = LineDataSet(entries, getString(R.string.statistic_fragment_distribution_description));
        dataSet.valueFormatter = IValueFormatter { value, _, _, _ -> String.format("%.1f%%", value * 100) }
        dataSet.color = ContextCompat.getColor(context, R.color.accent)
        dataSet.circleColors = listOf(ContextCompat.getColor(context, R.color.accent))

        var sum = 0
        val accumulated = IntArray(entries.size)
        for (i in 0..entries.lastIndex) {
            sum += stats[i]
            accumulated[i] = sum
        }

        val cumulativeDistribution = accumulated.map { it.toFloat() / statistics.numberOfMovies }.mapIndexed { index, i -> Entry(index.toFloat() + 1, i) }

        val dataSetCumulative = LineDataSet(cumulativeDistribution, getString(R.string.statistic_fragment_cumulative_distribution_description))
        dataSetCumulative.valueFormatter = IValueFormatter { value, _, _, _ -> String.format("%.1f%%", value * 100) }
        dataSetCumulative.color = ContextCompat.getColor(context, R.color.primary)
        dataSetCumulative.setCircleColor(ContextCompat.getColor(context, R.color.primary))

        statistics_chart.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        statistics_chart.xAxis?.isGranularityEnabled = true
        statistics_chart.xAxis?.granularity = 1f
        statistics_chart.xAxis?.mAxisMaximum = stats.size.toFloat()
        statistics_chart.axisRight?.isEnabled = false
        statistics_chart.xAxis?.labelCount = 15

        statistics_chart.legend?.isEnabled = true
        statistics_chart.legend?.isWordWrapEnabled = true
        statistics_chart.legend?.textSize = 14f

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(dataSet)
        dataSets.add(dataSetCumulative)

        statistics_chart.data = LineData(dataSets)
        statistics_chart.description?.isEnabled = false

        statistics_chart.invalidate()
    }

    /**
     * Returns an array with the number of correct predictions and the number of sneaks
     */
    private fun calcStatistic(): Pair<IntArray, SneakStatistics> {

        val predictions = SneakPeekDatabaseHelper.GetInstance(context).getMoviePredictions().reversed()
        val actualMovies = SneakPeekDatabaseHelper.GetInstance(context).getActualMovies()

        if (actualMovies.isEmpty()) {
            return Pair(kotlin.IntArray(0), SneakStatistics())
        }

        val correctPrediction = IntArray(predictions.map { it.movies.size }.max() ?: 15)

        val datasetSize = Math.min(actualMovies.lastIndex, predictions.lastIndex)

        for (i in 0..datasetSize) {

            val prediction = predictions[i]
            val movie = actualMovies[i]

            var indexOfCorrectPrediction = prediction.movies.find { it.title == movie.title }?.position ?: -1

            indexOfCorrectPrediction -= 1

            if (indexOfCorrectPrediction != -2) {
                correctPrediction[indexOfCorrectPrediction] += 1
            }
        }

        val numberOfStudios = SneakPeekDatabaseHelper.GetInstance(context).getStudios().size
        val numberOfPredictions = predictions.map { it.movies }.map { it.size }.sum()
        val numberOfUniquePredictions = predictions.flatMap { it.movies }.distinctBy { it.title }.size

        return Pair(correctPrediction,
                SneakStatistics(datasetSize, numberOfStudios, numberOfPredictions, numberOfUniquePredictions))
    }

    companion object {

        fun newInstance(): StatisticsFragment {
            return StatisticsFragment()
        }
    }

    data class SneakStatistics(val numberOfMovies: Int = -1,
                               val numberOfStudios: Int = -1,
                               val numberOfPredictions: Int = -1,
                               val numberOfUniquePredictions: Int = -1)
}