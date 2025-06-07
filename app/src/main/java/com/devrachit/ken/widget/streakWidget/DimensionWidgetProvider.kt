package com.devrachit.ken.widget.streakWidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.ActivityContent.MainActivity
import com.devrachit.ken.presentation.screens.dashboard.Widgets.ActivityData
import com.devrachit.ken.presentation.screens.dashboard.Widgets.DayModel
import com.devrachit.ken.presentation.screens.dashboard.Widgets.parseCalendarData
import com.devrachit.ken.utility.composeUtility.sdp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.times

class DimensionWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val sampleCalenderString2 =
                "{\"1735689600\": 1, \"1735776000\": 4, \"1735862400\": 1, \"1735948800\": 8, \"1736035200\": 3, \"1736121600\": 1, \"1736208000\": 1, \"1736294400\": 4, \"1736380800\": 1, \"1736467200\": 1, \"1736553600\": 1, \"1736640000\": 1, \"1736726400\": 1, \"1736812800\": 1, \"1736899200\": 1, \"1736985600\": 3, \"1737072000\": 2, \"1737158400\": 5, \"1737244800\": 1, \"1737331200\": 5, \"1737417600\": 1, \"1737504000\": 1, \"1737590400\": 1, \"1737676800\": 2, \"1737763200\": 2, \"1737936000\": 1, \"1738022400\": 9, \"1738108800\": 3, \"1738195200\": 1, \"1738281600\": 6, \"1738368000\": 3, \"1738454400\": 3, \"1738540800\": 5, \"1738627200\": 11, \"1738713600\": 9, \"1738800000\": 5, \"1738886400\": 1, \"1739059200\": 1, \"1739145600\": 1, \"1739232000\": 4, \"1739318400\": 6, \"1739404800\": 2, \"1739491200\": 1, \"1739577600\": 1, \"1739664000\": 1, \"1739750400\": 1, \"1739836800\": 1, \"1739923200\": 1, \"1740009600\": 2, \"1740096000\": 10, \"1740182400\": 1, \"1740268800\": 2, \"1740355200\": 1, \"1740441600\": 1, \"1740528000\": 3, \"1740614400\": 1, \"1740700800\": 1, \"1740787200\": 1, \"1740873600\": 1, \"1740960000\": 1, \"1741046400\": 1, \"1741132800\": 1, \"1741219200\": 1, \"1741305600\": 1, \"1741392000\": 1, \"1741478400\": 1, \"1741564800\": 1, \"1741651200\": 3, \"1741737600\": 1, \"1741824000\": 1, \"1741910400\": 2, \"1742083200\": 1, \"1742169600\": 1, \"1742256000\": 1, \"1742342400\": 1, \"1742428800\": 1, \"1742515200\": 2, \"1742601600\": 1, \"1742688000\": 1, \"1742774400\": 1, \"1742860800\": 5, \"1742947200\": 2, \"1743033600\": 1, \"1743120000\": 1, \"1743206400\": 7, \"1743292800\": 3, \"1743379200\": 1, \"1743465600\": 2, \"1743552000\": 3, \"1743638400\": 15, \"1743724800\": 1, \"1743811200\": 1, \"1743897600\": 1, \"1743984000\": 5, \"1744070400\": 1, \"1744156800\": 3, \"1744243200\": 13, \"1744329600\": 1, \"1744416000\": 1, \"1744502400\": 5, \"1744588800\": 3, \"1744675200\": 7, \"1744761600\": 6, \"1744848000\": 1, \"1744934400\": 1, \"1745020800\": 1, \"1745107200\": 1, \"1745193600\": 1, \"1745280000\": 3, \"1745366400\": 5, \"1745452800\": 2, \"1745539200\": 1, \"1745625600\": 1, \"1745712000\": 3, \"1745798400\": 1, \"1714348800\": 1, \"1714435200\": 1, \"1714521600\": 3, \"1714608000\": 3, \"1714694400\": 1, \"1714780800\": 1, \"1714867200\": 4, \"1714953600\": 1, \"1715040000\": 1, \"1715126400\": 1, \"1715212800\": 1, \"1715299200\": 2, \"1715385600\": 6, \"1715472000\": 4, \"1715558400\": 2, \"1715644800\": 1, \"1715731200\": 1, \"1715817600\": 1, \"1715904000\": 2, \"1715990400\": 1, \"1716076800\": 1, \"1716163200\": 1, \"1716249600\": 1, \"1716336000\": 1, \"1716422400\": 3, \"1716595200\": 7, \"1716681600\": 8, \"1716768000\": 7, \"1716854400\": 1, \"1717027200\": 1, \"1717113600\": 1, \"1717200000\": 1, \"1717286400\": 8, \"1717372800\": 1, \"1717545600\": 3, \"1717632000\": 2, \"1717718400\": 3, \"1717804800\": 5, \"1717891200\": 9, \"1717977600\": 2, \"1718064000\": 2, \"1718236800\": 6, \"1718323200\": 1, \"1718409600\": 2, \"1718496000\": 11, \"1718582400\": 15, \"1718668800\": 5, \"1718755200\": 11, \"1718841600\": 1, \"1718928000\": 1, \"1719014400\": 4, \"1719100800\": 11, \"1719187200\": 1, \"1719273600\": 1, \"1719360000\": 11, \"1719446400\": 5, \"1719532800\": 1, \"1719619200\": 1, \"1719705600\": 4, \"1719792000\": 1, \"1719878400\": 1, \"1719964800\": 1, \"1720051200\": 3, \"1720137600\": 1, \"1720224000\": 4, \"1720310400\": 12, \"1720396800\": 1, \"1720483200\": 1, \"1720656000\": 1, \"1720742400\": 1, \"1720828800\": 6, \"1720915200\": 9, \"1721001600\": 1, \"1721088000\": 2, \"1721174400\": 9, \"1721260800\": 3, \"1721347200\": 8, \"1721520000\": 1, \"1721606400\": 6, \"1721692800\": 20, \"1721779200\": 4, \"1721865600\": 3, \"1721952000\": 1, \"1722038400\": 7, \"1722124800\": 9, \"1722211200\": 2, \"1722297600\": 1, \"1722384000\": 1, \"1722470400\": 1, \"1722556800\": 1, \"1722643200\": 1, \"1722729600\": 4, \"1722816000\": 1, \"1722902400\": 3, \"1723075200\": 5, \"1723161600\": 1, \"1723248000\": 1, \"1723334400\": 1, \"1723420800\": 1, \"1723507200\": 1, \"1723593600\": 2, \"1723680000\": 2, \"1723766400\": 3, \"1723852800\": 8, \"1723939200\": 6, \"1724025600\": 1, \"1724112000\": 3, \"1724198400\": 3, \"1724371200\": 1, \"1724457600\": 1, \"1724544000\": 3, \"1724630400\": 1, \"1724716800\": 2, \"1724803200\": 6, \"1724889600\": 1, \"1724976000\": 1, \"1725062400\": 3, \"1725148800\": 2, \"1725235200\": 1, \"1725321600\": 1, \"1725408000\": 1, \"1725494400\": 1, \"1725580800\": 1, \"1725667200\": 1, \"1725753600\": 1, \"1725840000\": 1, \"1725926400\": 1, \"1726012800\": 2, \"1726099200\": 1, \"1726185600\": 1, \"1726272000\": 1, \"1726358400\": 12, \"1726444800\": 1, \"1726531200\": 1, \"1726617600\": 4, \"1726704000\": 1, \"1726790400\": 1, \"1726876800\": 1, \"1726963200\": 1, \"1727049600\": 1, \"1727136000\": 3, \"1727222400\": 1, \"1727308800\": 1, \"1727395200\": 1, \"1727481600\": 1, \"1727568000\": 1, \"1727654400\": 2, \"1727740800\": 1, \"1727827200\": 1, \"1728086400\": 1, \"1728172800\": 1, \"1728259200\": 1, \"1728432000\": 1, \"1728518400\": 1, \"1728604800\": 1, \"1728691200\": 1, \"1728777600\": 1, \"1728950400\": 1, \"1729036800\": 1, \"1729209600\": 4, \"1729296000\": 1, \"1729468800\": 3, \"1729555200\": 6, \"1729641600\": 11, \"1729728000\": 1, \"1729814400\": 1, \"1729900800\": 1, \"1729987200\": 5, \"1730073600\": 2, \"1730160000\": 1, \"1730246400\": 1, \"1730332800\": 3, \"1730419200\": 1, \"1730505600\": 2, \"1730592000\": 7, \"1730678400\": 7, \"1730764800\": 1, \"1730851200\": 1, \"1730937600\": 1, \"1731024000\": 1, \"1731110400\": 5, \"1731196800\": 1, \"1731369600\": 1, \"1731456000\": 1, \"1731542400\": 1, \"1731628800\": 3, \"1731715200\": 1, \"1731801600\": 1, \"1731888000\": 1, \"1731974400\": 1, \"1732060800\": 1, \"1732147200\": 1, \"1732233600\": 1, \"1732320000\": 1, \"1732406400\": 7, \"1732492800\": 1, \"1732579200\": 5, \"1732665600\": 1, \"1732752000\": 1, \"1732838400\": 1, \"1732924800\": 1, \"1733011200\": 1, \"1733097600\": 3, \"1733184000\": 1, \"1733270400\": 1, \"1733443200\": 1, \"1733529600\": 1, \"1733616000\": 1, \"1733702400\": 1, \"1733788800\": 1, \"1733875200\": 1, \"1733961600\": 1, \"1734048000\": 1, \"1734134400\": 1, \"1734220800\": 1, \"1734307200\": 1, \"1734393600\": 5, \"1734480000\": 1, \"1734566400\": 2, \"1734652800\": 1, \"1734739200\": 1, \"1734825600\": 1, \"1734912000\": 1, \"1734998400\": 2, \"1735084800\": 1, \"1735171200\": 1, \"1735257600\": 1, \"1735430400\": 1, \"1735516800\": 2, \"1735603200\": 1}"
            val rawActivityData = parseCalendarData(sampleCalenderString2)
            // Convert the parsed calendar data into a list of DayModel objects
            val dayModels = rawActivityData.map { (timestamp, contributions) ->
                val instant = Instant.ofEpochSecond(timestamp.toLong())
                val localDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    LocalDate.ofInstant(instant, ZoneOffset.UTC)
                } else {
                    instant.atZone(ZoneOffset.UTC).toLocalDate()
                }

                DayModel(
                    day = localDate.dayOfMonth,
                    month = localDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    year = localDate.year,
                    contributions = contributions,
                    monthPosition = localDate.monthValue - 1,
                    dayName = localDate.dayOfWeek.getDisplayName(
                        TextStyle.SHORT,
                        Locale.getDefault()
                    )
                )
            }.sortedWith(compareBy({ it.year }, { it.monthPosition }, { it.day }))
            // Create ActivityData object with sorted dayModels
            val activityData = ActivityData(dayModels)

            val currentTimestamp = 1745875744.831664
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId,
                activityData = activityData,
                currentTimestamp = currentTimestamp
            )
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {

        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        val sampleCalenderString =
            "{\"1735689600\": 3, \"1735862400\": 6, \"1735948800\": 8, \"1736208000\": 3, \"1736294400\": 1, \"1736380800\": 8, \"1736467200\": 4, \"1736553600\": 16, \"1736640000\": 8, \"1736726400\": 10, \"1736812800\": 2, \"1736899200\": 2, \"1736985600\": 5, \"1737072000\": 1, \"1737158400\": 7, \"1737244800\": 3, \"1737331200\": 3, \"1737417600\": 3, \"1737504000\": 2, \"1738368000\": 4, \"1738454400\": 12, \"1738800000\": 1, \"1739059200\": 1, \"1739145600\": 4, \"1739491200\": 2, \"1739750400\": 7, \"1739836800\": 1, \"1740009600\": 1, \"1740355200\": 3, \"1740441600\": 2, \"1740614400\": 6, \"1740700800\": 2, \"1740787200\": 7, \"1741046400\": 1, \"1741219200\": 1, \"1742774400\": 10, \"1743811200\": 1, \"1744761600\": 2, \"1714176000\": 5, \"1714348800\": 1, \"1714521600\": 2, \"1714608000\": 2, \"1714694400\": 3, \"1714780800\": 1, \"1714867200\": 9, \"1714953600\": 1, \"1715040000\": 2, \"1715126400\": 1, \"1715212800\": 3, \"1715299200\": 1, \"1715385600\": 1, \"1715472000\": 7, \"1715558400\": 2, \"1715644800\": 2, \"1715731200\": 1, \"1715817600\": 1, \"1715904000\": 1, \"1715990400\": 1, \"1716249600\": 1, \"1716336000\": 1, \"1716681600\": 2, \"1717113600\": 1, \"1717200000\": 1, \"1717632000\": 3, \"1718150400\": 1, \"1719187200\": 2, \"1720915200\": 1, \"1721606400\": 2, \"1723334400\": 1, \"1724889600\": 6, \"1730505600\": 6, \"1730592000\": 1, \"1730764800\": 3, \"1730937600\": 7, \"1731024000\": 4, \"1731628800\": 1, \"1731715200\": 3, \"1731974400\": 4, \"1732060800\": 1, \"1732147200\": 6, \"1732492800\": 2, \"1732665600\": 1, \"1732752000\": 5, \"1733011200\": 10, \"1733097600\": 3, \"1733184000\": 2, \"1733270400\": 3, \"1733356800\": 4, \"1733443200\": 6, \"1733616000\": 18, \"1733702400\": 7, \"1733788800\": 3, \"1733875200\": 9, \"1733961600\": 2, \"1734048000\": 18, \"1734134400\": 7, \"1734220800\": 1, \"1734566400\": 7, \"1734652800\": 1, \"1734739200\": 10, \"1734825600\": 13, \"1735171200\": 13, \"1735257600\": 6, \"1735430400\": 11, \"1735516800\": 4, \"1735603200\": 2}"

        val sampleCalenderString2 =
            "{\"1735689600\": 1, \"1735776000\": 4, \"1735862400\": 1, \"1735948800\": 8, \"1736035200\": 3, \"1736121600\": 1, \"1736208000\": 1, \"1736294400\": 4, \"1736380800\": 1, \"1736467200\": 1, \"1736553600\": 1, \"1736640000\": 1, \"1736726400\": 1, \"1736812800\": 1, \"1736899200\": 1, \"1736985600\": 3, \"1737072000\": 2, \"1737158400\": 5, \"1737244800\": 1, \"1737331200\": 5, \"1737417600\": 1, \"1737504000\": 1, \"1737590400\": 1, \"1737676800\": 2, \"1737763200\": 2, \"1737936000\": 1, \"1738022400\": 9, \"1738108800\": 3, \"1738195200\": 1, \"1738281600\": 6, \"1738368000\": 3, \"1738454400\": 3, \"1738540800\": 5, \"1738627200\": 11, \"1738713600\": 9, \"1738800000\": 5, \"1738886400\": 1, \"1739059200\": 1, \"1739145600\": 1, \"1739232000\": 4, \"1739318400\": 6, \"1739404800\": 2, \"1739491200\": 1, \"1739577600\": 1, \"1739664000\": 1, \"1739750400\": 1, \"1739836800\": 1, \"1739923200\": 1, \"1740009600\": 2, \"1740096000\": 10, \"1740182400\": 1, \"1740268800\": 2, \"1740355200\": 1, \"1740441600\": 1, \"1740528000\": 3, \"1740614400\": 1, \"1740700800\": 1, \"1740787200\": 1, \"1740873600\": 1, \"1740960000\": 1, \"1741046400\": 1, \"1741132800\": 1, \"1741219200\": 1, \"1741305600\": 1, \"1741392000\": 1, \"1741478400\": 1, \"1741564800\": 1, \"1741651200\": 3, \"1741737600\": 1, \"1741824000\": 1, \"1741910400\": 2, \"1742083200\": 1, \"1742169600\": 1, \"1742256000\": 1, \"1742342400\": 1, \"1742428800\": 1, \"1742515200\": 2, \"1742601600\": 1, \"1742688000\": 1, \"1742774400\": 1, \"1742860800\": 5, \"1742947200\": 2, \"1743033600\": 1, \"1743120000\": 1, \"1743206400\": 7, \"1743292800\": 3, \"1743379200\": 1, \"1743465600\": 2, \"1743552000\": 3, \"1743638400\": 15, \"1743724800\": 1, \"1743811200\": 1, \"1743897600\": 1, \"1743984000\": 5, \"1744070400\": 1, \"1744156800\": 3, \"1744243200\": 13, \"1744329600\": 1, \"1744416000\": 1, \"1744502400\": 5, \"1744588800\": 3, \"1744675200\": 7, \"1744761600\": 6, \"1744848000\": 1, \"1744934400\": 1, \"1745020800\": 1, \"1745107200\": 1, \"1745193600\": 1, \"1745280000\": 3, \"1745366400\": 5, \"1745452800\": 2, \"1745539200\": 1, \"1745625600\": 1, \"1745712000\": 3, \"1745798400\": 1, \"1714348800\": 1, \"1714435200\": 1, \"1714521600\": 3, \"1714608000\": 3, \"1714694400\": 1, \"1714780800\": 1, \"1714867200\": 4, \"1714953600\": 1, \"1715040000\": 1, \"1715126400\": 1, \"1715212800\": 1, \"1715299200\": 2, \"1715385600\": 6, \"1715472000\": 4, \"1715558400\": 2, \"1715644800\": 1, \"1715731200\": 1, \"1715817600\": 1, \"1715904000\": 2, \"1715990400\": 1, \"1716076800\": 1, \"1716163200\": 1, \"1716249600\": 1, \"1716336000\": 1, \"1716422400\": 3, \"1716595200\": 7, \"1716681600\": 8, \"1716768000\": 7, \"1716854400\": 1, \"1717027200\": 1, \"1717113600\": 1, \"1717200000\": 1, \"1717286400\": 8, \"1717372800\": 1, \"1717545600\": 3, \"1717632000\": 2, \"1717718400\": 3, \"1717804800\": 5, \"1717891200\": 9, \"1717977600\": 2, \"1718064000\": 2, \"1718236800\": 6, \"1718323200\": 1, \"1718409600\": 2, \"1718496000\": 11, \"1718582400\": 15, \"1718668800\": 5, \"1718755200\": 11, \"1718841600\": 1, \"1718928000\": 1, \"1719014400\": 4, \"1719100800\": 11, \"1719187200\": 1, \"1719273600\": 1, \"1719360000\": 11, \"1719446400\": 5, \"1719532800\": 1, \"1719619200\": 1, \"1719705600\": 4, \"1719792000\": 1, \"1719878400\": 1, \"1719964800\": 1, \"1720051200\": 3, \"1720137600\": 1, \"1720224000\": 4, \"1720310400\": 12, \"1720396800\": 1, \"1720483200\": 1, \"1720656000\": 1, \"1720742400\": 1, \"1720828800\": 6, \"1720915200\": 9, \"1721001600\": 1, \"1721088000\": 2, \"1721174400\": 9, \"1721260800\": 3, \"1721347200\": 8, \"1721520000\": 1, \"1721606400\": 6, \"1721692800\": 20, \"1721779200\": 4, \"1721865600\": 3, \"1721952000\": 1, \"1722038400\": 7, \"1722124800\": 9, \"1722211200\": 2, \"1722297600\": 1, \"1722384000\": 1, \"1722470400\": 1, \"1722556800\": 1, \"1722643200\": 1, \"1722729600\": 4, \"1722816000\": 1, \"1722902400\": 3, \"1723075200\": 5, \"1723161600\": 1, \"1723248000\": 1, \"1723334400\": 1, \"1723420800\": 1, \"1723507200\": 1, \"1723593600\": 2, \"1723680000\": 2, \"1723766400\": 3, \"1723852800\": 8, \"1723939200\": 6, \"1724025600\": 1, \"1724112000\": 3, \"1724198400\": 3, \"1724371200\": 1, \"1724457600\": 1, \"1724544000\": 3, \"1724630400\": 1, \"1724716800\": 2, \"1724803200\": 6, \"1724889600\": 1, \"1724976000\": 1, \"1725062400\": 3, \"1725148800\": 2, \"1725235200\": 1, \"1725321600\": 1, \"1725408000\": 1, \"1725494400\": 1, \"1725580800\": 1, \"1725667200\": 1, \"1725753600\": 1, \"1725840000\": 1, \"1725926400\": 1, \"1726012800\": 2, \"1726099200\": 1, \"1726185600\": 1, \"1726272000\": 1, \"1726358400\": 12, \"1726444800\": 1, \"1726531200\": 1, \"1726617600\": 4, \"1726704000\": 1, \"1726790400\": 1, \"1726876800\": 1, \"1726963200\": 1, \"1727049600\": 1, \"1727136000\": 3, \"1727222400\": 1, \"1727308800\": 1, \"1727395200\": 1, \"1727481600\": 1, \"1727568000\": 1, \"1727654400\": 2, \"1727740800\": 1, \"1727827200\": 1, \"1728086400\": 1, \"1728172800\": 1, \"1728259200\": 1, \"1728432000\": 1, \"1728518400\": 1, \"1728604800\": 1, \"1728691200\": 1, \"1728777600\": 1, \"1728950400\": 1, \"1729036800\": 1, \"1729209600\": 4, \"1729296000\": 1, \"1729468800\": 3, \"1729555200\": 6, \"1729641600\": 11, \"1729728000\": 1, \"1729814400\": 1, \"1729900800\": 1, \"1729987200\": 5, \"1730073600\": 2, \"1730160000\": 1, \"1730246400\": 1, \"1730332800\": 3, \"1730419200\": 1, \"1730505600\": 2, \"1730592000\": 7, \"1730678400\": 7, \"1730764800\": 1, \"1730851200\": 1, \"1730937600\": 1, \"1731024000\": 1, \"1731110400\": 5, \"1731196800\": 1, \"1731369600\": 1, \"1731456000\": 1, \"1731542400\": 1, \"1731628800\": 3, \"1731715200\": 1, \"1731801600\": 1, \"1731888000\": 1, \"1731974400\": 1, \"1732060800\": 1, \"1732147200\": 1, \"1732233600\": 1, \"1732320000\": 1, \"1732406400\": 7, \"1732492800\": 1, \"1732579200\": 5, \"1732665600\": 1, \"1732752000\": 1, \"1732838400\": 1, \"1732924800\": 1, \"1733011200\": 1, \"1733097600\": 3, \"1733184000\": 1, \"1733270400\": 1, \"1733443200\": 1, \"1733529600\": 1, \"1733616000\": 1, \"1733702400\": 1, \"1733788800\": 1, \"1733875200\": 1, \"1733961600\": 1, \"1734048000\": 1, \"1734134400\": 1, \"1734220800\": 1, \"1734307200\": 1, \"1734393600\": 5, \"1734480000\": 1, \"1734566400\": 2, \"1734652800\": 1, \"1734739200\": 1, \"1734825600\": 1, \"1734912000\": 1, \"1734998400\": 2, \"1735084800\": 1, \"1735171200\": 1, \"1735257600\": 1, \"1735430400\": 1, \"1735516800\": 2, \"1735603200\": 1}"
        val rawActivityData = parseCalendarData(sampleCalenderString)
        // Convert the parsed calendar data into a list of DayModel objects
        val dayModels = rawActivityData.map { (timestamp, contributions) ->
            val instant = Instant.ofEpochSecond(timestamp.toLong())
            val localDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                LocalDate.ofInstant(instant, ZoneOffset.UTC)
            } else {
                instant.atZone(ZoneOffset.UTC).toLocalDate()
            }

            DayModel(
                day = localDate.dayOfMonth,
                month = localDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                year = localDate.year,
                contributions = contributions,
                monthPosition = localDate.monthValue - 1,
                dayName = localDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            )
        }.sortedWith(compareBy({ it.year }, { it.monthPosition }, { it.day }))
        // Create ActivityData object with sorted dayModels
        val activityData = ActivityData(dayModels)

        val currentTimestamp = 1745875744.831664
        updateAppWidget(
            context,
            appWidgetManager,
            appWidgetId,
            activityData = activityData,
            currentTimestamp = currentTimestamp
        )
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            activityData: ActivityData,
            currentTimestamp: Double,
        ) {
            val options = appWidgetManager.getAppWidgetOptions(appWidgetId)

            val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
            val maxWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
            val maxHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)

            val views = RemoteViews(context.packageName, R.layout.dimension_widget_layout)

            val scale = 2.0f
            val density = context.resources.displayMetrics.density
            val maxBitmapSize = 1500
            val bitmapWidth = minOf((minWidth * scale * density).toInt(), maxBitmapSize)
            val bitmapHeight = minOf((minHeight * scale * density).toInt(), maxBitmapSize)

            val bitmap = createBitmap(width = bitmapWidth, height = bitmapHeight)
            val canvas = Canvas(bitmap)

            // Color definitions
            val heatmap1Color = context.getColor(R.color.heatmap1)
            val heatmap2Color = context.getColor(R.color.heatmap2)
            val heatmap3Color = context.getColor(R.color.heatmap3)
            val heatmap4Color = context.getColor(R.color.heatmap4)
            val grayColor = androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.2f).toArgb()

            // Calculate responsive cell size based on widget height
            val availableHeight = bitmapHeight - 60 * density // Reserve space for text
            val cellSize = (availableHeight / 8).toFloat() // 7 days + spacing
            val cellSpacing = cellSize * 0.2f
            val cornerRadius = cellSize * 0.3f

            // Calculate how many months can fit horizontally
            val monthWidth = 6 * (cellSize + cellSpacing) // Max 6 columns per month
            val availableWidth = bitmapWidth - 40 * density // Padding
            val maxMonths = maxOf(1, (availableWidth / monthWidth).toInt())

            val currentDate = run {
                val instant = Instant.ofEpochSecond(currentTimestamp.toLong())
                instant.atZone(ZoneOffset.UTC).toLocalDate()
            }

            // Create paint for text
            val textPaint = Paint().apply {
                color = context.getColor(android.R.color.white)
                textSize = cellSize * 0.8f
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }

            // Calculate starting position
            val startX = 20 * density
            val startY = 40 * density
            var currentX = startX

            // Draw months (current month + previous months that fit)
            for (monthOffset in 0 until maxMonths) {
                val targetDate = currentDate.minusMonths(monthOffset.toLong())

                // Draw month label
                val monthText =
                    targetDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                canvas.drawText(
                    monthText,
                    currentX + monthWidth / 2,
                    startY - 10 * density,
                    textPaint
                )

                // Get month data
                val monthData = activityData.dayModels.filter { dayModel ->
                    val localDate =
                        LocalDate.of(dayModel.year, dayModel.monthPosition + 1, dayModel.day)
                    localDate.month == targetDate.month && localDate.year == targetDate.year
                }
                val dayToActivityMap = monthData.associateBy { it.day }

                // Calculate month layout
                val firstDayOfMonth = LocalDate.of(targetDate.year, targetDate.month, 1)
                val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
                val daysInMonth = targetDate.lengthOfMonth()
                val maxDayToShow = if (monthOffset == 0) currentDate.dayOfMonth else daysInMonth

                // Draw cells for this month
                for (day in 1..maxDayToShow) {
                    val dayModel = dayToActivityMap[day]
                    val color = when {
                        dayModel == null -> grayColor
                        dayModel.contributions > 10 -> heatmap1Color
                        dayModel.contributions > 5 -> heatmap2Color
                        dayModel.contributions > 2 -> heatmap3Color
                        else -> heatmap4Color
                    }

                    val dayOfWeek = (firstDayOfWeek + day - 1) % 7
                    val weekNumber = (firstDayOfWeek + day - 1) / 7

                    val cellX = currentX + weekNumber * (cellSize + cellSpacing)
                    val cellY = startY + dayOfWeek * (cellSize + cellSpacing)

                    val rect = RectF(
                        cellX,
                        cellY,
                        cellX + cellSize,
                        cellY + cellSize
                    )

                    val paint = Paint().apply {
                        this.color = color
                        style = Paint.Style.FILL
                        isAntiAlias = true
                    }
                    canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
                }

                currentX += monthWidth

                // Break if we've reached the width limit
                if (currentX + monthWidth > bitmapWidth) break
            }

            // Set the bitmap to the ImageView
            views.setImageViewBitmap(R.id.widget_drawing, bitmap)

            // Format dimension text based on widget size
            val dimensionText = when {
                minWidth == maxWidth && minHeight == maxHeight -> {
                    "${minWidth}dp × ${minHeight}dp"
                }
                minWidth < 200 && minHeight < 100 -> {
                    "${minWidth}×${minHeight}dp"
                }
                else -> {
                    "Size: ${minWidth}dp × ${minHeight}dp"
                }
            }
            views.setTextViewText(R.id.username, dimensionText)

            // Adjust text size based on widget size
            val textSize = when {
                minWidth < 150 -> 8f
                minWidth < 250 -> 10f
                else -> 12f
            }
            views.setTextViewTextSize(
                R.id.username,
                TypedValue.COMPLEX_UNIT_SP,
                textSize
            )

            // Add click functionality to open the app
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun drawBlueSquareBox(
            canvas: Canvas,
            canvasWidth: Int,
            canvasHeight: Int,
            widgetWidth: Int,
            widgetHeight: Int
        ) {
            // Calculate rectangle dimensions: width = 70% of height
            val rectangleHeight = minOf(widgetWidth, widgetHeight).toFloat()
            val rectangleWidth = rectangleHeight * 0.7f

            // Scale the rectangle to fit within the canvas
            val scaleFactor = minOf(
                canvasWidth.toFloat() / rectangleWidth,
                canvasHeight.toFloat() / rectangleHeight
            )
            val scaledWidth = rectangleWidth * scaleFactor
            val scaledHeight = rectangleHeight * scaleFactor

            // Create paint for the rectangles
            val paint = Paint().apply {
                this.color = Color.WHITE
                style = Paint.Style.FILL
                isAntiAlias = true
            }

            // Check space availability for multiple rectangles
            val totalWidthForFourRectangles = scaledWidth * 4
            val totalWidthForThreeRectangles = scaledWidth * 3
            val totalWidthForTwoRectangles = scaledWidth * 2
            val canShowFourRectangles = canvasWidth >= totalWidthForFourRectangles
            val canShowThreeRectangles = canvasWidth >= totalWidthForThreeRectangles
            val canShowTwoRectangles = canvasWidth >= totalWidthForTwoRectangles

            when {
                canShowFourRectangles -> {
                    // Draw four rectangles side by side
                    val spacing = (canvasWidth - totalWidthForFourRectangles) / 5f
                    val topPosition = (canvasHeight - scaledHeight) / 2f

                    // First rectangle
                    val firstLeft = spacing
                    val firstRect = RectF(
                        firstLeft,
                        topPosition,
                        firstLeft + scaledWidth,
                        topPosition + scaledHeight
                    )
                    canvas.drawRect(firstRect, paint)

                    // Second rectangle
                    val secondLeft = firstLeft + scaledWidth + spacing
                    val secondRect = RectF(
                        secondLeft,
                        topPosition,
                        secondLeft + scaledWidth,
                        topPosition + scaledHeight
                    )
                    canvas.drawRect(secondRect, paint)

                    // Third rectangle
                    val thirdLeft = secondLeft + scaledWidth + spacing
                    val thirdRect = RectF(
                        thirdLeft,
                        topPosition,
                        thirdLeft + scaledWidth,
                        topPosition + scaledHeight
                    )
                    canvas.drawRect(thirdRect, paint)

                    // Fourth rectangle
                    val fourthLeft = thirdLeft + scaledWidth + spacing
                    val fourthRect = RectF(
                        fourthLeft,
                        topPosition,
                        fourthLeft + scaledWidth,
                        topPosition + scaledHeight
                    )
                    canvas.drawRect(fourthRect, paint)
                }

                canShowThreeRectangles -> {
                    // Draw three rectangles side by side
                    val spacing = (canvasWidth - totalWidthForThreeRectangles) / 4f
                    val topPosition = (canvasHeight - scaledHeight) / 2f

                    // First rectangle (left)
                    val firstRectLeft = spacing
                    val firstRectTop = topPosition
                    val firstRectRight = firstRectLeft + scaledWidth
                    val firstRectBottom = firstRectTop + scaledHeight
                    val firstRect =
                        RectF(firstRectLeft, firstRectTop, firstRectRight, firstRectBottom)
                    canvas.drawRect(firstRect, paint)

                    // Second rectangle (middle)
                    val secondRectLeft = firstRectRight + spacing
                    val secondRectTop = topPosition
                    val secondRectRight = secondRectLeft + scaledWidth
                    val secondRectBottom = secondRectTop + scaledHeight
                    val secondRect =
                        RectF(secondRectLeft, secondRectTop, secondRectRight, secondRectBottom)
                    canvas.drawRect(secondRect, paint)

                    // Third rectangle (right)
                    val thirdRectLeft = secondRectRight + spacing
                    val thirdRectTop = topPosition
                    val thirdRectRight = thirdRectLeft + scaledWidth
                    val thirdRectBottom = thirdRectTop + scaledHeight
                    val thirdRect =
                        RectF(thirdRectLeft, thirdRectTop, thirdRectRight, thirdRectBottom)
                    canvas.drawRect(thirdRect, paint)
                }

                canShowTwoRectangles -> {
                    // Draw two rectangles side by side
                    val spacing = (canvasWidth - totalWidthForTwoRectangles) / 3f
                    val topPosition = (canvasHeight - scaledHeight) / 2f

                    // First rectangle (left)
                    val firstRectLeft = spacing
                    val firstRectTop = topPosition
                    val firstRectRight = firstRectLeft + scaledWidth
                    val firstRectBottom = firstRectTop + scaledHeight
                    val firstRect =
                        RectF(firstRectLeft, firstRectTop, firstRectRight, firstRectBottom)
                    canvas.drawRect(firstRect, paint)

                    // Second rectangle (right)
                    val secondRectLeft = firstRectRight + spacing
                    val secondRectTop = topPosition
                    val secondRectRight = secondRectLeft + scaledWidth
                    val secondRectBottom = secondRectTop + scaledHeight
                    val secondRect =
                        RectF(secondRectLeft, secondRectTop, secondRectRight, secondRectBottom)
                    canvas.drawRect(secondRect, paint)
                }

                else -> {
                    // Draw single rectangle centered
                    val left = (canvasWidth - scaledWidth) / 2f
                    val top = (canvasHeight - scaledHeight) / 2f
                    val right = left + scaledWidth
                    val bottom = top + scaledHeight
                    val rect = RectF(left, top, right, bottom)
                    canvas.drawRect(rect, paint)
                }
            }
        }
    }
}
