@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<String> sendLocations(@RequestBody List<LocationDto> locations) {
        boolean success = locationService.sendLocationsToServer(locations);
        if (success) {
            return ResponseEntity.ok("Locations sent successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send locations");
        }
    }
}
