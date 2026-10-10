package pe.edu.unsch.clientes.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unsch.clientes.dto.ClienteRequestDTO;
import pe.edu.unsch.clientes.dto.ClienteResponseDTO;
import pe.edu.unsch.clientes.entity.Cliente;
import pe.edu.unsch.clientes.exception.DniAlreadyExistsException;
import pe.edu.unsch.clientes.exception.ResourceNotFoundException;
import pe.edu.unsch.clientes.repository.ClienteRepository;

import java.util.Collections;
import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteResponseDTO crearCliente(ClienteRequestDTO request) {
        if (clienteRepository.existsByDni(request.dni())) {
            throw new DniAlreadyExistsException("Ya existe un cliente registrado con el DNI: " + request.dni());
        }
        Cliente cliente = new Cliente(null, request.dni(), request.nombre(), request.email());
        return mapToDTO(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> buscarClientes(String dni, String nombre) {
        if (dni != null && !dni.isBlank()) {
            return clienteRepository.findByDni(dni.trim())
                    .map(c -> List.of(mapToDTO(c)))
                    .orElse(Collections.emptyList());
        }
        if (nombre != null && !nombre.isBlank()) {
            return clienteRepository.findByNombreContainingIgnoreCase(nombre.trim())
                    .stream()
                    .map(this::mapToDTO)
                    .toList();
        }
        return clienteRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return mapToDTO(cliente);
    }

    @Transactional
    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        if (!cliente.getDni().equals(request.dni()) && clienteRepository.existsByDni(request.dni())) {
            throw new DniAlreadyExistsException("El DNI " + request.dni() + " ya pertenece a otro cliente.");
        }

        cliente.setDni(request.dni());
        cliente.setNombre(request.nombre());
        cliente.setEmail(request.email());
        return mapToDTO(clienteRepository.save(cliente));
    }

    @Transactional
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    private ClienteResponseDTO mapToDTO(Cliente c) {
        return new ClienteResponseDTO(c.getId(), c.getDni(), c.getNombre(), c.getEmail(), c.getFechaCreacion());
    }
}