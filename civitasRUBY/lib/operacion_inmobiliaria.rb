#encoding: utf-8

class OperacionInmobiliaria
  attr_reader :num_propiedad, :gestion
  
  def initialize (gestion, num_propiedad)
    @num_propiedad = num_propiedad
    @gestion = gestion
  end
end