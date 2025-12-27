package com.outsourcing.presentation.navigation

sealed class Destination(
    val label: String,
    val route: String,
) {
    data object Scan : Destination(
        "Scan", "scan",
    )

    data object JobList : Destination(
        "Job List", "job_list",
    )

    data object JobDetail: Destination(
        "Job Detail", "job_detail"
    )
}