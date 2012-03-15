require 'rubygems'
require 'sinatra'
require 'json'

playerid = 0
players = {}

get '/newplayer' do
  playerid += 1
  pstr = "player" + playerid.to_s
  if playerid == 1 
    players[pstr] = {"player" => pstr, "color" => "redtank.png"}
  else
    players[pstr] = {"player" => pstr, "color" => "bluetank.png"}
  end

  return players[pstr].to_json
end

post '/update/:player' do 
  request.body.rewind
  data = JSON.parse request.body.read
  players[params[:player]]["posx"] = data['posx']
  players[params[:player]]["posy"] = data['posy']
  p = players.select{|k,v| k != params[:player] }
  return "[]" if p.size == 0
  p = p[0][1]
  p = [p]
  return p.to_json
end

get '/players' do
  players.to_json
end
