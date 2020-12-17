var map;
function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: parseFloat(personLocation.lat), lng: parseFloat(personLocation.lng) },
		zoom: 15,
		scrollwheel: false
	});
  var image ={url: '/bus-stop.png',
              scaledSize: new google.maps.Size(50,50)};
	for (i=0; i<busLocations.length; i++){
    //bus_marker defined
		var bus_marker = new google.maps.Marker({
		    position: { lat: parseFloat(busLocations[i].LATITUDE), lng: parseFloat(busLocations[i].LONGITUDE) },
		    map: map,
        icon: image
		});
    var infowindow = new google.maps.InfoWindow();
    content = 'Vehicle is ' + busLocations[i].VEHICLE
    google.maps.event.addListener(bus_marker,'click', (function(bus_marker,content,infowindow){ 
        return function() {
            infowindow.setContent(content);
            infowindow.open(map,bus_marker);
        };
    })(bus_marker,content,infowindow));  
	}

  var person_image={url: '/pedestrian.png',
                    scaledSize: new google.maps.Size(50,50)};
  var person_marker = new google.maps.Marker({
              position: { lat: parseFloat(personLocation.lat), lng: parseFloat(personLocation.lng) },
              map: map,
              icon: person_image});

  //google.maps.event.trigger(map, 'reize');
}
/*var map;
function initMap() {
	map = new google.maps.Map(document.getElementById('map'), {
		center: { lat: parseFloat(busLocations[0].LATITUDE), lng: parseFloat(busLocations[0].LONGITUDE) },
		zoom: 15,
		scrollwheel: false
	});
	
	for (i=0; i<busLocations.length; i++){
		var marker = new google.maps.Marker({
		    position: { lat: parseFloat(busLocations[i].LATITUDE), lng: parseFloat(busLocations[i].LONGITUDE) },
		    map: map,
		});
	}
}*/
