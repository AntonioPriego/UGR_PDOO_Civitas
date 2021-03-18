#encoding: utf-8

module Civitas 
  class MazoSorpresas
    
    attr_accessor :usadas, :ultimaSorpresa, :sorpresas
    
    def initialize(_debug=false)
      @sorpresas = Array.new
      @barajada = false
      @usadas = 0
      @cartasEspeciales = Array.new
      @ultimaSorpresa = nil
      @debug = _debug
    end
    
    public
    def alMazo (nueva)
      if (!@barajada)
        @sorpresas.push(nueva)
      end
    end
    
    #Devuelve la siguiente sorpresa, barajando si no está barajado
    def siguiente
      if (!@barajada || @usadas==@sorpresas.size) && !@debug
        @sorpresas.shuffle #Baraja
        @usadas   = 0
        @barajada = true
      end
      
      @usadas += 1
      
      @ultimaSorpresa = @sorpresas[0] # [1, 2, 3]
      @sorpresas << @sorpresas[0]     # [1, 2, 3, 1]
      @sorpresas.delete_at(0)         # [2, 3, 1]
      
      return @ultimaSorpresa      
    end
    
    #Inhabilita carta sorpresa de mazo, convirtiéndola en especial
    def inhabilitarCartaEspecial(sorpresa)
      for carta in @sorpresas
        if @sopresas==sorpresa
          @cartasEspeciales << carta
          @sorpresas.delete(carta)
          
          Diario.instance.ocurreEvento("Carta especial inhabilitada")
        end
      end
    end
    
    #Habilita carta sorpresa de mazo, convirtiéndola en carta común
    def habilitarCartaEspecial(sorpresa)
      for carta in @cartasEspeciales
        if @cartasEspeciales==sorpresa
          @sorpresas << sorpresa
          @cartasEspeciales.delete(carta)
          
          Diario.instance.ocurreEvento("Carta especial habilitada")
        end
      end
    end
    
  end
end