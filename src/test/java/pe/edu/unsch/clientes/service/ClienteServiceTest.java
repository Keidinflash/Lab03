package pe.edu.unsch.clientes.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.unsch.clientes.dto.ClienteRequestDTO;
import pe.edu.unsch.clientes.dto.ClienteResponseDTO;
import pe.edu.unsch.clientes.entity.Cliente;
import pe.edu.unsch.clientes.exception.DniAlreadyExistsException;
import pe.edu.unsch.clientes.repository.ClienteRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    @DisplayName("Debe registrar un cliente satisfactoriamente cuando el DNI no existe")
    void crearCliente_Exitoso() {
        ClienteRequestDTO req = new ClienteRequestDTO("12345678", "Carlos Mendoza", "carlos@unsch.edu.pe");
        when(clienteRepository.existsByDni("12345678")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> {
            Cliente c = inv.getArgument(0);
            c.setId(1L);
            return c;
        });

        ClienteResponseDTO res = clienteService.crearCliente(req);

        assertNotNull(res);
        assertEquals(1L, res.id());
        assertEquals("12345678", res.dni());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Debe lanzar DniAlreadyExistsException (409) cuando el DNI ya esta registrado")
    void crearCliente_DniDuplicado() {
        ClienteRequestDTO req = new ClienteRequestDTO("12345678", "Carlos Mendoza", "carlos@unsch.edu.pe");
        when(clienteRepository.existsByDni("12345678")).thenReturn(true);

        assertThrows(DniAlreadyExistsException.class, () -> clienteService.crearCliente(req));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }
}