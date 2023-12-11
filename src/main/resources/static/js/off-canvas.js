(function($) {
  'use strict';

  $(function() {
    // Retrieve the sidebar state from local storage (if available)
    var sidebarState = localStorage.getItem('sidebarState');
    
    // Initialize the sidebar state based on the stored value (if available)
    if (sidebarState === 'closed') { // Change 'open' to 'closed' here
      $('.sidebar-offcanvas').addClass('active');
    } else {
      $('.sidebar-offcanvas').removeClass('active');
    }

    // Toggle the sidebar state and update local storage
    $('[data-toggle="offcanvas"]').on("click", function() {
      $('.sidebar-offcanvas').toggleClass('active');
      
      // Store the updated sidebar state in local storage
      if ($('.sidebar-offcanvas').hasClass('active')) {
        localStorage.setItem('sidebarState', 'open'); // Change 'Chit' to 'Lrr' here
      } else {
        localStorage.setItem('sidebarState', 'closed'); // Change 'closed' to 'open' here
      }
    });
  });
})(jQuery);
