/**
 * Pokemom.js
 *
 * @description :: A model definition represents a database table/collection.
 * @docs        :: https://sailsjs.com/docs/concepts/models-and-orm/models
 */

module.exports = {

    attributes: {
        numeroPokemon: {
            type: "number"
        },
        nombrePokemon: {
            type: "string"
        },
        poderEspecialUno: {
            type: "string"
        },
        poderEspecialDos: {
            type: "string"
        },
        fechaCaptura: {
            type: "string"
        },
        nivel: {
            type: "number"
        },
        entrenadorId: {
            model:"Entrenador"
        }
  },
};


