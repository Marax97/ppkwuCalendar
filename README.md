# ppkwuCalendar

GetWeeiaEvents
Returns object in format of ical filled with events in weeia calendar for particular month.
•	URL
/ppkwu /calendar/{year}/{month}
•	Method:
GET
•	URL Params
Required:
year=[integer]
month=[integer]
•	Data Params
None
•	Success Response:
o	Code: 200
Content:  BEGIN:VCALENDAR PRODID:-//Ben Fortuna//iCal4j 1.0//EN VERSION:2.0 CALSCALE:GREGORIAN BEGIN:VEVENT DTSTAMP:20191105T161329Z DTSTART;VALUE=DATE:20191104 SUMMARY:First Step to Fields Medal UID:e95ce345-ab74-40d1-90d0-5b760f446c92 END:VEVENT BEGIN:VEVENT DTSTAMP:20191105T161329Z DTSTART;VALUE=DATE:20191106 SUMMARY:First Step to Success UID:ca76aa92-dcde-4fbd-82e0-b7cb199b5f36 END:VEVENT BEGIN:VEVENT DTSTAMP:20191105T161329Z DTSTART;VALUE=DATE:20191108 SUMMARY:First Step to Nobel Prize UID:6eb272e5-2bde-4736-a265-fcbd5fe5ec78 END:VEVENT BEGIN:VEVENT DTSTAMP:20191105T161329Z DTSTART;VALUE=DATE:20191115 SUMMARY:Fascynująca Fizyka - poziom podstawowy UID:6aec0596-d203-4a32-8eac-9b10f123e00e END:VEVENT END:VCALENDAR
•	Error Response:
o	Code: 404 NOT FOUND
Content:  Error in loading weeia calendar

