jQuery(document).ready(function($) {
    jQuery('#dateTime').datetimepicker({
        format: "Y-m-d H:m"
    });

    jQuery('#startDate').datetimepicker({
        timepicker: false,
        format: "Y-m-d"
    });

    jQuery('#endDate').datetimepicker({
        timepicker: false,
        format: "Y-m-d"
    });

    jQuery('#startTime').datetimepicker({
        datepicker: false,
        format: "H:m"
    });

    jQuery('#endTime').datetimepicker({
        datepicker: false,
        format: "H:m"
    });
});