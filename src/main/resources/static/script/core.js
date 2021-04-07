$(document).ready(function () {
    // Wire the modal toggle buttons
    $(".modal-toggle").each(function() {
        // Get the corresponding modal
        let selector = $(this).data("modal-selector");
        let modal = $(selector);

        // Set this button's click event to show the modal
        $(this).click(function() {
            modal.show();
        });
    });

    // Wire the modal close buttons
    $(".modal-close").each(function() {
        // Get the corresponding modal
        let modal = $(this).closest(".modal");

        // Set this button's click event to hide the modal
        $(this).click(function() {
            modal.hide();
        });
    });

    // Wire clicking outside the modals
    let modals = $(".modal");
    $(window).click(function(e) {
        modals.each(function() {
            if (e.target === this) {
                $(this).hide();
            }
        })
    });
});