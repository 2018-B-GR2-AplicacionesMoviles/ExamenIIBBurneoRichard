/**
 * Entrenador.js
 *
 * @description :: A model definition represents a database table/collection.
 * @docs        :: https://sailsjs.com/docs/concepts/models-and-orm/models
 */

module.exports={
  attributes: {
    nombre:{
      type: "string"
      },
    apellido:{
      type: "string"
      },
    fechaNacimiento:{
       type: "string"
      },
    numeroMedallas:{
       type:"number"
       },
    campeonActual:{
          type:"boolean"
       },
    pokemones:{
             collection:"Pokemon",
           via:"entrenadorId"
        }
    },
};
