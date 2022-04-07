## Docker Setup
#### MongoDB
<p>runs on port 27017, run this command in the terminal after docker-compose up</p>
<code>
rs.initiate( { _id : 'rs0', members: [ { _id : 0, host : "mongo:27017" } ] } )
</code>

#### Maildev
<p>SMTP server runs on port 1025, UI is on port 1080</p>