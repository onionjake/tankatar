require 'rubygems'
require 'sinatra'
require 'json'

players = {}

post '/players/:player' do
  players[params[:player]] = true
end

get '/players' do
  players.to_json
end


get '/hi' do 
  "Hello everyone!"
end
